package com.example.there.aircraftradar.flightdetails

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.example.there.aircraftradar.data.model.FlightDetails
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class FlightDetailsViewModel @Inject constructor(
        private val interactor: FlightDetailsContract.Interactor
): ViewModel(), FlightDetailsContract.ViewModel {

    private val flightDetailsLoadingDisposables = CompositeDisposable()

    val flightDetailsResponse = MutableLiveData<FlightDetails>()

    override fun onCleared() {
        super.onCleared()
        flightDetailsLoadingDisposables.clear()
    }

    override fun loadFlightDetails(flightId: String) {
        flightDetailsLoadingDisposables.add(interactor.loadFlightDetails(flightId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ flightDetailsResponse.value = it }, { Log.e("loadFlightDetails", it?.message) }))
    }
}