package com.example.flightdetails.ui.fragment

import android.arch.lifecycle.LiveData
import com.example.coreandroid.model.FlightDetails

interface SharesFlightDetails {
    val flightDetails: LiveData<FlightDetails?>
}