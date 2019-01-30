package com.example.map.domain.repo

import com.example.core.data.Result
import com.example.coreandroid.model.Flight

interface IMapRepository {
    suspend fun loadFlights(): Result<List<Flight>>
}