package com.example.there.aircraftradar.data.model

import android.annotation.SuppressLint
import com.example.there.aircraftradar.util.extension.formattedString
import com.example.there.aircraftradar.util.extension.toDate
import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName
import io.mironov.smuggler.AutoParcelable

@SuppressLint("ParcelCreator")
data class FlightDetails(
        val identification: Identification,
        val status: Status,
        val level: String,
        val aircraft: Aircraft,
        val airline: Airline,
        val owner: String?,
        val airspace: String?,
        val airport: Airport,
        val flightHistory: FlightHistory,
        val availability: List<String>,
        val time: Time,
        val trail: List<Trail>,
        val firstTimestamp: Int?,
        val s: String
) : AutoParcelable {
    val imageUrl: String?
        get() = when {
            aircraft.images.large.isNotEmpty() -> aircraft.images.large.first().src
            aircraft.images.medium.isNotEmpty() -> aircraft.images.medium.first().src
            aircraft.images.thumbnails.isNotEmpty() -> aircraft.images.thumbnails.first().src
            else -> null
        }

    val info: List<Pair<String, String>>
        get() = listOf(
                Pair("From:", airport.origin?.name ?: "No destination airport info."),
                Pair("To:", airport.destination?.name ?: "No origin airport info."),
                Pair("Airline:", airline.name),
                Pair("Aircraft:", aircraft.model.text),
                Pair("Departure:", time.scheduled?.departure?.toDate()?.toString()
                        ?: "Unknown departure time."),
                Pair("Arrival:", time.scheduled?.arrival?.toDate()?.toString()
                        ?: "Unknown arrival time.")
        )
}

@SuppressLint("ParcelCreator")
data class Time(
        val scheduled: Scheduled?,
        val real: Real?,
        val estimated: Estimated?,
        val other: Other?,
        val historical: Historical?
) : AutoParcelable

@SuppressLint("ParcelCreator")
data class Real(
        val departure: Long?,
        val arrival: Long?
) : AutoParcelable

@SuppressLint("ParcelCreator")
data class Scheduled(
        val departure: Long?,
        val arrival: Long?
) : AutoParcelable

@SuppressLint("ParcelCreator")
data class Historical(
        val flighttime: String,
        val delay: String
) : AutoParcelable

@SuppressLint("ParcelCreator")
data class Estimated(
        val departure: Long?,
        val arrival: Long?
) : AutoParcelable

@SuppressLint("ParcelCreator")
data class Other(
        val eta: Int?,
        val updated: Int?
) : AutoParcelable

@SuppressLint("ParcelCreator")
data class Airline(
        val name: String,
        val short: String,
        val code: Code,
        val url: String
) : AutoParcelable

@SuppressLint("ParcelCreator")
data class Code(
        val iata: String,
        val icao: String
) : AutoParcelable

@SuppressLint("ParcelCreator")
data class AirportInfo(
        val name: String,
        val code: Code,
        val position: Position,
        val timezone: Timezone,
        val visible: Boolean,
        val website: String,
        val info: Info?
) : AutoParcelable {
    val latLng: LatLng
        get() = position.latLng
}

@SuppressLint("ParcelCreator")
data class Info(
        val terminal: String?,
        val baggage: String?,
        val gate: String?
) : AutoParcelable

@SuppressLint("ParcelCreator")
data class Region(
        val city: String
) : AutoParcelable

@SuppressLint("ParcelCreator")
data class Timezone(
        val name: String,
        val offset: Int?,
        val offsetHours: String,
        val abbr: String,
        val abbrName: String,
        val isDst: Boolean
) : AutoParcelable

@SuppressLint("ParcelCreator")
data class Position(
        val latitude: Double,
        val longitude: Double,
        val altitude: Int?,
        val country: Country,
        val region: Region
) : AutoParcelable {
    val latLng: LatLng
        get() = LatLng(latitude, longitude)
}

@SuppressLint("ParcelCreator")
data class Country(
        val name: String,
        val code: String
) : AutoParcelable

@SuppressLint("ParcelCreator")
data class FlightHistory(
        @SerializedName("aircraft") val aircrafts: List<AircraftHistory>
) : AutoParcelable

@SuppressLint("ParcelCreator")
data class AircraftHistory(
        val identification: Identification,
        val airport: Airport,
        val time: Time
) : AutoParcelable

@SuppressLint("ParcelCreator")
data class Airport(
        val origin: AirportInfo?,
        val destination: AirportInfo?
) : AutoParcelable

@SuppressLint("ParcelCreator")
data class Trail(
        val lat: Double,
        val lng: Double,
        val alt: Int,
        val spd: Int,
        val ts: Int,
        val hd: Int
) : AutoParcelable {
    val latLng: LatLng
        get() = LatLng(lat, lng)

    val title: String
        get() = latLng.formattedString

    val snippet: String
        get() = "Height: $alt feet\nSpeed: $spd knots"
}


@SuppressLint("ParcelCreator")
data class Aircraft(
        val model: Model,
        val registration: String,
        val hex: String,
        val age: Int?,
        val msn: String?,
        val images: Images
) : AutoParcelable

@SuppressLint("ParcelCreator")
data class Model(
        val code: String,
        val text: String
) : AutoParcelable

@SuppressLint("ParcelCreator")
data class Images(
        val thumbnails: List<Thumbnail>,
        val medium: List<Thumbnail>,
        val large: List<Thumbnail>
) : AutoParcelable

@SuppressLint("ParcelCreator")
data class Thumbnail(
        val src: String,
        val link: String,
        val copyright: String,
        val source: String
) : AutoParcelable

@SuppressLint("ParcelCreator")
data class Identification(
        val id: String,
        val row: Long?,
        val number: Number,
        val callsign: String?
) : AutoParcelable

@SuppressLint("ParcelCreator")
data class Number(
        val default: String,
        val alternative: String?
) : AutoParcelable

@SuppressLint("ParcelCreator")
data class Status(
        val live: Boolean,
        val text: String,
        val icon: String,
        val ambiguous: Boolean,
        val generic: Generic
) : AutoParcelable

@SuppressLint("ParcelCreator")
data class Generic(
        val status: GenericStatus,
        val eventTime: EventTime
) : AutoParcelable

@SuppressLint("ParcelCreator")
data class GenericStatus(
        val text: String,
        val color: String,
        val type: String
) : AutoParcelable

@SuppressLint("ParcelCreator")
data class EventTime(
        val utc: Int?,
        val local: Int?
) : AutoParcelable