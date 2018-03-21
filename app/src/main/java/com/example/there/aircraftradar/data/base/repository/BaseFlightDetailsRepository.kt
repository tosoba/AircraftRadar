package com.example.there.aircraftradar.data.base.repository

interface BaseFlightDetailsRepository {
    fun loadFlightDetails(flightId: String)
}