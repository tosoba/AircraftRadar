package com.example.there.aircraftradar.map

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.example.there.aircraftradar.data.model.Flight
import com.google.android.gms.maps.model.LatLngBounds
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MapViewModel @Inject constructor(
        private val interactor: MapContract.Interactor
) : ViewModel(), MapContract.ViewModel {

    private val flightLoadingDisposables = CompositeDisposable()

    override val flightsResponse = MutableLiveData<List<Flight>>()

    override fun onCleared() {
        flightLoadingDisposables.clear()
        super.onCleared()
    }

    override fun loadFlightsInBounds(bounds: LatLngBounds?) {
        flightLoadingDisposables.clear()
        flightLoadingDisposables.add(interactor.loadFlightsInBounds(bounds)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ flightsResponse.value = it }, {
                    Log.e("loadFlightsInBounds", it?.message ?: "Error loading flights.")
                }))
    }
}