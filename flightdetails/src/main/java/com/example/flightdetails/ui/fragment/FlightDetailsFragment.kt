package com.example.flightdetails.ui.fragment

import android.arch.lifecycle.LiveData
import android.os.Bundle
import android.support.v4.app.Fragment
import com.example.coreandroid.model.Flight
import com.example.coreandroid.model.FlightDetails
import com.example.flightdetails.ui.FlightDetailsContract

abstract class FlightDetailsFragment : Fragment() {

    protected val flightDetails: LiveData<FlightDetails?>
        get() = (activity as FlightDetailsContract.View).flightDetails

    protected lateinit var flight: Flight

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            flight = it.getParcelable(ARG_FLIGHT)!!
        }
    }

    fun putArguments(flight: Flight) {
        arguments = Bundle().apply {
            putParcelable(ARG_FLIGHT, flight)
        }
    }

    companion object {
        const val ARG_FLIGHT = "ARG_FLIGHT"
    }
}