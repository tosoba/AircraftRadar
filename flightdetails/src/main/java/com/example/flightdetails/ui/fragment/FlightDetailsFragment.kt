package com.example.flightdetails.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import com.example.coreandroid.model.Flight
import com.example.coreandroid.model.FlightDetails

abstract class FlightDetailsFragment : Fragment() {

    abstract var flightDetails: FlightDetails?
    protected lateinit var flight: Flight

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            flight = it.getParcelable(ARG_FLIGHT)!!
        }
    }

    fun putArguments(flight: Flight) {
        val args = Bundle()
        args.putParcelable(ARG_FLIGHT, flight)
        arguments = args
    }

    companion object {
        const val ARG_FLIGHT = "ARG_FLIGHT"
    }
}