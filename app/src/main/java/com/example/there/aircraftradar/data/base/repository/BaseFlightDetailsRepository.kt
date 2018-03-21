package com.example.there.aircraftradar.data.base.repository

import com.example.there.aircraftradar.data.impl.flightdetails.FlightDetailsResponse
import io.reactivex.Observable

interface BaseFlightDetailsRepository {
    fun loadFlightDetails(flightId: String): Observable<FlightDetailsResponse>
}