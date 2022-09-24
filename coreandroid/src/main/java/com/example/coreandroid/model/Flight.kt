package com.example.coreandroid.model

import android.os.Parcelable
import com.example.coreandroid.ext.formattedString
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Flight(
    val id: String,
    val modeSCode: String,
    val latitude: Double,
    val longitude: Double,
    val bearing: Int, // in degrees
    val altitude: Int, // in feet
    val speed: Int, // in knots
    val squawkCode: String, // https://en.wikipedia.org/wiki/Transponder_(aeronautics)
    val radar: String, // F24 "radar" data source ID
    val model: String,
    val registration: String,
    val timestamp: Int,
    val origin: String, // origin airport IATA code
    val destination: String, // destination airport IATA code
    val flight: String, // flight number
    val isOnGround: Boolean,
    val rateOfClimb: Int, // ft/min
    val callsign: String,
    val isGlider: Boolean
) : Parcelable {

    val position: LatLng
        get() = LatLng(latitude, longitude)

    val info: List<Pair<String, String>>
        get() = listOf(
            Pair("Flight:", callsign),
            Pair("Position:", position.formattedString),
            Pair("Route:", "From $origin to $destination"),
            Pair("Speed:", "$speed knots"),
            Pair("Altitude:", "$altitude feet"),
            Pair("Model:", model)
        )
}