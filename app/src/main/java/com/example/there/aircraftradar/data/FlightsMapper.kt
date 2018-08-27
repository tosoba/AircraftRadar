package com.example.there.aircraftradar.data

import com.example.there.aircraftradar.data.model.Flight
import com.example.there.aircraftradar.domain.Mapper
import org.json.JSONObject

object FlightsMapper: Mapper<String, List<Flight>> {
    override fun fromEntity(entity: String): List<Flight> {
        val response = JSONObject(entity)
        val flights = ArrayList<Flight>()

        val keys = response.keys().asSequence().drop(2)
        keys.forEach { id ->
            val flight = response.getJSONArray(id)
            if (flight.length() < 18) {
                return@forEach
            }
            flights.add(Flight(
                    id = id,
                    modeSCode = flight.getString(0),
                    latitude = flight.getDouble(1),
                    longitude = flight.getDouble(2),
                    bearing = flight.getInt(3),
                    altitude = flight.getInt(4),
                    speed = flight.getInt(5),
                    squawkCode = flight.getString(6),
                    radar = flight.getString(7),
                    model = flight.getString(8),
                    registration = flight.getString(9),
                    timestamp = flight.getInt(10),
                    origin = flight.getString(11),
                    destination = flight.getString(12),
                    flight = flight.getString(13),
                    isOnGround = flight.getInt(14) == 1,
                    rateOfClimb = flight.getInt(15),
                    callsign = flight.getString(16),
                    isGlider = flight.getInt(17) == 1
            ))
        }

        return flights
    }
}