package com.example.map.data

import com.example.api.FlightRadarApi
import com.example.api.mapper.FlightsMapper
import com.example.core.data.Result
import com.example.core.data.mapSuccess
import com.example.coreandroid.model.Flight
import com.example.coreandroid.retrofit.awaitResult
import com.example.map.domain.repo.IMapRepository
import javax.inject.Inject

class MapRepository @Inject constructor(
        private val api: FlightRadarApi
) : IMapRepository {

    override suspend fun loadFlights(): Result<List<Flight>> = api.loadFlights()
            .awaitResult()
            .mapSuccess(FlightsMapper::fromEntity)
}