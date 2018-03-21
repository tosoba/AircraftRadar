package com.example.there.aircraftradar.data.impl.flightdetails

import com.google.gson.annotations.SerializedName


data class FlightDetailsResponse(
		val identification: Identification,
		val status: Status,
		val level: String,
		val aircraft: Aircraft,
		val airline: Airline,
		val owner: String?,
		val airspace: String?,
		val airport: Airport,
		val flightHistory: FlightHistory,
		//val ems: Any,
		val availability: List<String>,
		val time: Time,
		val trail: List<Trail>,
		val firstTimestamp: Int,
		val s: String
)

data class Time(
		val scheduled: Scheduled?,
		val real: Real?,
		val estimated: Estimated?,
		val other: Other?,
		val historical: Historical?
)

data class Real(
		val departure: Int?,
		val arrival: Int?
)

data class Scheduled(
		val departure: Int,
		val arrival: Int
)

data class Historical(
		val flighttime: String,
		val delay: String
)

data class Estimated(
		val departure: Int?,
		val arrival: Int?
)

data class Other(
		val eta: Int,
		val updated: Int
)

data class Airline(
		val name: String,
		val short: String,
		val code: Code,
		val url: String
)

data class Code(
		val iata: String,
		val icao: String
)

data class Destination(
		val name: String,
		val code: Code,
		val position: Position,
		val timezone: Timezone,
		val visible: Boolean,
		val website: String,
		val info: Info?
)

data class Info(
		val terminal: String?,
		val baggage: String?,
		val gate: String?
)

data class Region(
		val city: String
)

data class Origin(
		val name: String,
		val code: Code,
		val position: Position,
		val timezone: Timezone,
		val visible: Boolean,
		val website: String,
		val info: Info?
)

data class Timezone(
		val name: String,
		val offset: Int,
		val offsetHours: String,
		val abbr: String,
		val abbrName: String,
		val isDst: Boolean
)

data class Position(
		val latitude: Double,
		val longitude: Double,
		val altitude: Int,
		val country: Country,
		val region: Region
)

data class Country(
		val name: String,
		val code: String
)

data class FlightHistory(
		@SerializedName("aircraft") val aircrafts: List<AircraftHistory>
)

data class AircraftHistory(
		val identification: Identification,
		val airport: Airport,
		val time: Time
)

data class Airport(
		val origin: Origin,
		val destination: Destination
)

data class Trail(
		val lat: Double,
		val lng: Double,
		val alt: Int,
		val spd: Int,
		val ts: Int,
		val hd: Int
)

data class Aircraft(
		val model: Model,
		val registration: String,
		val hex: String,
		val age: Int,
		val msn: String?,
		val images: Images
)

data class Model(
		val code: String,
		val text: String
)

data class Images(
		val thumbnails: List<Thumbnail>,
		val medium: List<Medium>,
		val large: List<Large>
)

data class Thumbnail(
		val src: String,
		val link: String,
		val copyright: String,
		val source: String
)

data class Large(
		val src: String,
		val link: String,
		val copyright: String,
		val source: String
)

data class Medium(
		val src: String,
		val link: String,
		val copyright: String,
		val source: String
)

data class Identification(
		val id: String,
		val row: Long?,
		val number: Number,
		val callsign: String?
)

data class Number(
		val default: String,
		val alternative: String?
)

data class Status(
		val live: Boolean,
		val text: String,
		val icon: String,
//		val estimated: Any,
		val ambiguous: Boolean,
		val generic: Generic
)

data class Generic(
		val status: GenericStatus,
		val eventTime: EventTime
)

data class GenericStatus(
		val text: String,
		val color: String,
		val type: String
)

data class EventTime(
		val utc: Int,
		val local: Int
)