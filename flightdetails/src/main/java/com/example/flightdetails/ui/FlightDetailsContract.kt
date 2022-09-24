package com.example.flightdetails.ui

import androidx.lifecycle.LiveData
import com.example.coreandroid.model.FlightDetails
import com.example.flightdetails.domain.feature.FlightDetailsFeature
import com.example.flightdetails.ui.fragment.SharesFlightDetails

interface FlightDetailsContract {
    interface ViewModel {
        val state: LiveData<FlightDetailsFeature.State>
        val flightDetails: FlightDetails?
        val flightDetailsLoaded: Boolean
        fun loadFlightDetails(flightId: String)
    }

    interface View : SharesFlightDetails
}