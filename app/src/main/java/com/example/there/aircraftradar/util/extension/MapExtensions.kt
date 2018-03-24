package com.example.there.aircraftradar.util.extension

import com.androidmapsextensions.GoogleMap
import com.androidmapsextensions.Marker
import com.androidmapsextensions.MarkerOptions
import com.example.there.aircraftradar.R
import com.example.there.aircraftradar.data.impl.flights.Flight
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLngBounds

private val flightIcon: BitmapDescriptor by lazy { BitmapDescriptorFactory.fromResource(R.drawable.plane) }

val GoogleMap.bounds: LatLngBounds
    get() = projection.visibleRegion.latLngBounds

fun GoogleMap.addFlight(flight: Flight): Marker {
    return addMarker(MarkerOptions()
            .title(flight.callsign)
            .position(flight.position)
            .icon(flightIcon)
            .rotation(flight.bearing.toFloat()))
}