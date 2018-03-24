package com.example.there.aircraftradar.map.flight

import com.androidmapsextensions.Marker
import com.example.there.aircraftradar.data.impl.flights.Flight

data class FlightMarker(
        var flight: Flight,
        var marker: Marker
)