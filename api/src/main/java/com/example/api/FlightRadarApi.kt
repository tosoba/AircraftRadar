package com.example.api

import com.example.coreandroid.model.FlightDetails
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FlightRadarApi {
    @GET("zones/fcgi/feed.js")
    fun loadFlights(): Call<String>

    @GET("clickhandler")
    fun loadFlightDetails(
        @Query("flight") flightId: String,
        @Query("version") version: String = "1.5"
    ): Call<FlightDetails>
}