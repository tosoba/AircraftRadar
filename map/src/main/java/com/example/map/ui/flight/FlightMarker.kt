package com.example.map.ui.flight

import com.androidmapsextensions.Marker
import com.example.coreandroid.model.Flight

data class FlightMarker(
    var flight: Flight,
    var marker: Marker
)