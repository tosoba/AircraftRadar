package com.example.there.aircraftradar.data

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface FlightRadarApiService {
    @GET("zones/fcgi/feed.js")
    fun loadFlights(@Query("bounds") bounds: String): Observable<String>

    @GET("clickhandler")
    fun loadFlightDetails(@Query("flight") flightId: String,
                          @Query("version") version: String = "1.5")
}