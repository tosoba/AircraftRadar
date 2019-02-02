package com.example.map.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.example.map.domain.feature.FlightsFeature
import javax.inject.Inject


class MapViewModel @Inject constructor(
        private val flightsFeature: FlightsFeature
) : ViewModel(), MapContract.ViewModel {

    override val state: LiveData<FlightsFeature.State>
        get() = flightsFeature.liveState

    override fun loadFlights() {
        flightsFeature.dispatch(FlightsFeature.Action.Load)
    }

    override fun onCleared() {
        flightsFeature.dispose()
        super.onCleared()
    }
}