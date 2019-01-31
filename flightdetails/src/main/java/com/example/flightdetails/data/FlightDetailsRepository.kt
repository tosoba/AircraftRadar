package com.example.flightdetails.data

import com.example.api.FlightRadarApi
import com.example.core.data.Result
import com.example.coreandroid.model.FlightDetails
import com.example.coreandroid.retrofit.awaitResult
import com.example.flightdetails.domain.repo.IFlightDetailsRepository
import javax.inject.Inject

class FlightDetailsRepository @Inject constructor(
        private val api: FlightRadarApi
) : IFlightDetailsRepository {

    override suspend fun loadFlightDetails(
            flightId: String
    ): Result<FlightDetails> = api.loadFlightDetails(flightId).awaitResult()
}