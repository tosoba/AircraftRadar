package com.example.there.aircraftradar.flightdetails

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

class FlightDetailsViewModelFactory(private val interactor: FlightDetailsContract.Interactor) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FlightDetailsViewModel::class.java)) {
            return FlightDetailsViewModel(interactor) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}