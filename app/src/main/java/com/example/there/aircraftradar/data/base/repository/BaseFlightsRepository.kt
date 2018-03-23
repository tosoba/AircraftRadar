package com.example.there.aircraftradar.data.base.repository

import com.example.there.aircraftradar.data.impl.flights.Flight
import com.google.android.gms.maps.model.LatLngBounds
import io.reactivex.Observable

interface BaseFlightsRepository {
    fun loadFlights(bounds: LatLngBounds?): Observable<List<Flight>>
}