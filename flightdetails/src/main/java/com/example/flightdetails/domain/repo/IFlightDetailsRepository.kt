package com.example.flightdetails.domain.repo

import com.example.core.data.Result
import com.example.coreandroid.model.FlightDetails

interface IFlightDetailsRepository {
    suspend fun loadFlightDetails(flightId: String): Result<FlightDetails>
}