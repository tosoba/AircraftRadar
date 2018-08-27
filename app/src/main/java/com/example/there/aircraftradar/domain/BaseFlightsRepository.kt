package com.example.there.aircraftradar.domain

import com.example.there.aircraftradar.data.model.Flight
import com.example.there.aircraftradar.data.model.FlightDetails
import com.google.android.gms.maps.model.LatLngBounds
import io.reactivex.Observable

interface BaseFlightsRepository {
    fun loadFlights(bounds: LatLngBounds?): Observable<List<Flight>>
    fun loadFlightDetails(flightId: String): Observable<FlightDetails>
}