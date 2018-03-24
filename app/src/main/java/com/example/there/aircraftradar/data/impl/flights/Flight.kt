package com.example.there.aircraftradar.data.impl.flights

import android.os.Parcel
import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng

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
): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readDouble(),
            parcel.readDouble(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readByte() != 0.toByte())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(modeSCode)
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
        parcel.writeInt(bearing)
        parcel.writeInt(altitude)
        parcel.writeInt(speed)
        parcel.writeString(squawkCode)
        parcel.writeString(radar)
        parcel.writeString(model)
        parcel.writeString(registration)
        parcel.writeInt(timestamp)
        parcel.writeString(origin)
        parcel.writeString(destination)
        parcel.writeString(flight)
        parcel.writeByte(if (isOnGround) 1 else 0)
        parcel.writeInt(rateOfClimb)
        parcel.writeString(callsign)
        parcel.writeByte(if (isGlider) 1 else 0)
    }

    override fun describeContents(): Int = 0

    val position: LatLng
        get() = LatLng(latitude, longitude)

    companion object CREATOR : Parcelable.Creator<Flight> {
        override fun createFromParcel(parcel: Parcel): Flight = Flight(parcel)
        override fun newArray(size: Int): Array<Flight?> = arrayOfNulls(size)
    }
}