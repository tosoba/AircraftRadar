package com.example.there.aircraftradar.flightdetails.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import com.example.there.aircraftradar.data.model.Flight
import com.example.there.aircraftradar.data.model.FlightDetails

abstract class FlightDetailsFragment : Fragment() {

    abstract var flightDetails: FlightDetails?
    protected lateinit var flight: Flight

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            flight = arguments.getParcelable(ARG_FLIGHT)
            flightDetails = arguments.getParcelable(ARG_FLIGHT_DETAILS)
        }
    }

    fun putArguments(flight: Flight, flightDetails: FlightDetails?) {
        val args = Bundle()
        args.putParcelable(ARG_FLIGHT, flight)
        args.putParcelable(ARG_FLIGHT_DETAILS, flightDetails)
        arguments = args
    }

    companion object {
        const val ARG_FLIGHT = "ARG_FLIGHT"
        const val ARG_FLIGHT_DETAILS = "ARG_FLIGHT_DETAILS"
    }
}