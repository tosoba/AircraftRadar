package com.example.flightdetails.ui.fragment

import androidx.lifecycle.LiveData
import com.example.coreandroid.model.FlightDetails

interface SharesFlightDetails {
    val flightDetails: LiveData<FlightDetails?>
}