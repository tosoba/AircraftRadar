package com.example.there.aircraftradar.data.impl.flightdetails

import com.example.there.aircraftradar.data.FlightRadarApiService
import com.example.there.aircraftradar.data.base.repository.BaseFlightDetailsRepository
import io.reactivex.Observable

class FlightDetailsRepository(private val service: FlightRadarApiService): BaseFlightDetailsRepository {
    override fun loadFlightDetails(flightId: String): Observable<FlightDetails> = service.loadFlightDetails(flightId)
}