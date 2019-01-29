package com.example.api

import com.example.core.model.FlightDetails
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FlightRadarApi {
    @GET("zones/fcgi/feed.js")
    fun loadFlights(
            @Query("bounds") bounds: String? = null
    ): Call<String>

    @GET("clickhandler")
    fun loadFlightDetails(
            @Query("flight") flightId: String,
            @Query("version") version: String = "1.5"
    ): Call<FlightDetails>
}