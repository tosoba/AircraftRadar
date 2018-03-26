package com.example.there.aircraftradar.data.base.repository

import com.example.there.aircraftradar.data.impl.flightdetails.FlightDetails
import io.reactivex.Observable

interface BaseFlightDetailsRepository {
    fun loadFlightDetails(flightId: String): Observable<FlightDetails>
}