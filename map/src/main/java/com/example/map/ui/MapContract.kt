package com.example.map.ui

import androidx.lifecycle.LiveData
import com.example.map.domain.feature.FlightsFeature

interface MapContract {
    interface ViewModel {
        val state: LiveData<FlightsFeature.State>
        fun loadFlights()
    }
}