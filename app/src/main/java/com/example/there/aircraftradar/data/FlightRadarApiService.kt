package com.example.there.aircraftradar.data

import com.example.there.aircraftradar.data.model.FlightDetails
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface FlightRadarApiService {

    @GET("zones/fcgi/feed.js")
    fun loadFlights(@Query("bounds") bounds: String? = null): Single<String>

    @GET("clickhandler")
    fun loadFlightDetails(
            @Query("flight") flightId: String,
            @Query("version") version: String = "1.5"
    ): Single<FlightDetails>
}