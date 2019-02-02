package com.example.map.ui

import android.arch.lifecycle.LiveData
import com.example.map.domain.feature.FlightsFeature

interface MapContract {
    interface ViewModel {
        val state: LiveData<FlightsFeature.State>
        fun loadFlights()
    }
}