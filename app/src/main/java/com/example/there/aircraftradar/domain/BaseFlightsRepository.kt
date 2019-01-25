package com.example.there.aircraftradar.domain

import com.example.there.aircraftradar.data.model.Flight
import com.example.there.aircraftradar.data.model.FlightDetails
import com.google.android.gms.maps.model.LatLngBounds
import io.reactivex.Observable
import io.reactivex.Single

interface BaseFlightsRepository {
    fun loadFlights(bounds: LatLngBounds?): Single<List<Flight>>
    fun loadFlightDetails(flightId: String): Single<FlightDetails>
}