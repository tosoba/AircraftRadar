package com.example.flightdetails.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.example.coreandroid.model.FlightDetails
import com.example.flightdetails.domain.feature.FlightDetailsFeature
import javax.inject.Inject


class FlightDetailsViewModel @Inject constructor(
        private val flightDetailsFeature: FlightDetailsFeature
) : ViewModel(), FlightDetailsContract.ViewModel {

    override val state: LiveData<FlightDetailsFeature.State>
        get() = flightDetailsFeature.liveState

    override val flightDetails: FlightDetails?
        get() = flightDetailsFeature.currentState.flightDetails

    override val flightDetailsLoaded: Boolean
        get() = flightDetailsFeature.liveState.value?.flightDetails != null

    override fun loadFlightDetails(flightId: String) {
        flightDetailsFeature.dispatch(FlightDetailsFeature.Action.Load(flightId))
    }

    override fun onCleared() {
        flightDetailsFeature.dispose()
        super.onCleared()
    }
}