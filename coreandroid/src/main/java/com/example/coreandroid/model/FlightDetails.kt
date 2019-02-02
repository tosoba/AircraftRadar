package com.example.coreandroid.model

import android.os.Parcelable
import com.example.coreandroid.ext.formattedString
import com.example.coreandroid.ext.toDate
import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class FlightDetails(
        val identification: Identification?,
        val status: Status?,
        val level: String?,
        val aircraft: Aircraft?,
        val airline: Airline?,
        val owner: String?,
        val airspace: String?,
        val airport: Airport?,
        val flightHistory: FlightHistory?,
        val availability: List<String>?,
        val time: Time?,
        val trail: List<Trail>?,
        val firstTimestamp: Int?,
        val s: String?
) : Parcelable {
    val imageUrl: String?
        get() = if (aircraft?.images == null) null
        else when {
            aircraft.images.large.isNotEmpty() -> aircraft.images.large.first().src
            aircraft.images.medium.isNotEmpty() -> aircraft.images.medium.first().src
            aircraft.images.thumbnails.isNotEmpty() -> aircraft.images.thumbnails.first().src
            else -> null
        }

    val info: List<Pair<String, String>>
        get() = listOf(
                Pair("From:", airport?.origin?.name ?: "No destination airport info."),
                Pair("To:", airport?.destination?.name ?: "No origin airport info."),
                Pair("Airline:", airline?.name ?: "No airline info."),
                Pair("Aircraft:", aircraft?.model?.text ?: "No aircraft info."),
                Pair("Departure:", time?.scheduled?.departure?.toDate()?.toString()
                        ?: "Unknown departure time."),
                Pair("Arrival:", time?.scheduled?.arrival?.toDate()?.toString()
                        ?: "Unknown arrival time.")
        )
}

@Parcelize
data class Time(
        val scheduled: Scheduled?,
        val real: Real?,
        val estimated: Estimated?,
        val other: Other?,
        val historical: Historical?
) : Parcelable

@Parcelize
data class Real(
        val departure: Long?,
        val arrival: Long?
) : Parcelable

@Parcelize
data class Scheduled(
        val departure: Long?,
        val arrival: Long?
) : Parcelable

@Parcelize
data class Historical(
        val flighttime: String?,
        val delay: String?
) : Parcelable

@Parcelize
data class Estimated(
        val departure: Long?,
        val arrival: Long?
) : Parcelable

@Parcelize
data class Other(
        val eta: Int?,
        val updated: Int?
) : Parcelable

@Parcelize
data class Airline(
        val name: String?,
        val short: String?,
        val code: Code?,
        val url: String?
) : Parcelable

@Parcelize
data class Code(
        val iata: String?,
        val icao: String?
) : Parcelable

@Parcelize
data class AirportInfo(
        val name: String?,
        val code: Code?,
        val position: Position?,
        val timezone: Timezone?,
        val visible: Boolean?,
        val website: String?,
        val info: Info?
) : Parcelable {
    val latLng: LatLng?
        get() = position?.latLng
}

@Parcelize
data class Info(
        val terminal: String?,
        val baggage: String?,
        val gate: String?
) : Parcelable

@Parcelize
data class Region(
        val city: String?
) : Parcelable

@Parcelize
data class Timezone(
        val name: String,
        val offset: Int?,
        val offsetHours: String,
        val abbr: String,
        val abbrName: String,
        val isDst: Boolean
) : Parcelable

@Parcelize
data class Position(
        val latitude: Double,
        val longitude: Double,
        val altitude: Int?,
        val country: Country,
        val region: Region
) : Parcelable {
    val latLng: LatLng
        get() = LatLng(latitude, longitude)
}

@Parcelize
data class Country(
        val name: String,
        val code: String
) : Parcelable

@Parcelize
data class FlightHistory(
        @SerializedName("aircraft") val aircrafts: List<AircraftHistory>
) : Parcelable

@Parcelize
data class AircraftHistory(
        val identification: Identification,
        val airport: Airport,
        val time: Time
) : Parcelable

@Parcelize
data class Airport(
        val origin: AirportInfo?,
        val destination: AirportInfo?
) : Parcelable

@Parcelize
data class Trail(
        val lat: Double,
        val lng: Double,
        val alt: Int,
        val spd: Int,
        val ts: Int,
        val hd: Int
) : Parcelable {
    val latLng: LatLng
        get() = LatLng(lat, lng)

    val title: String
        get() = latLng.formattedString

    val snippet: String
        get() = "Height: $alt feet\nSpeed: $spd knots"
}


@Parcelize
data class Aircraft(
        val model: Model,
        val registration: String,
        val hex: String,
        val age: Int?,
        val msn: String?,
        val images: Images
) : Parcelable

@Parcelize
data class Model(
        val code: String,
        val text: String
) : Parcelable

@Parcelize
data class Images(
        val thumbnails: List<Thumbnail>,
        val medium: List<Thumbnail>,
        val large: List<Thumbnail>
) : Parcelable

@Parcelize
data class Thumbnail(
        val src: String,
        val link: String,
        val copyright: String,
        val source: String
) : Parcelable

@Parcelize
data class Identification(
        val id: String,
        val row: Long?,
        val number: Number,
        val callsign: String?
) : Parcelable

@Parcelize
data class Number(
        val default: String,
        val alternative: String?
) : Parcelable

@Parcelize
data class Status(
        val live: Boolean,
        val text: String,
        val icon: String,
        val ambiguous: Boolean,
        val generic: Generic
) : Parcelable

@Parcelize
data class Generic(
        val status: GenericStatus,
        val eventTime: EventTime
) : Parcelable

@Parcelize
data class GenericStatus(
        val text: String,
        val color: String,
        val type: String
) : Parcelable

@Parcelize
data class EventTime(
        val utc: Int?,
        val local: Int?
) : Parcelable