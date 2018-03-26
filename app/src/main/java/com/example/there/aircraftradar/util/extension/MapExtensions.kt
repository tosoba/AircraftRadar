package com.example.there.aircraftradar.util.extension

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.util.Log
import com.androidmapsextensions.GoogleMap
import com.androidmapsextensions.Marker
import com.androidmapsextensions.MarkerOptions
import com.androidmapsextensions.PolylineOptions
import com.example.there.aircraftradar.R
import com.example.there.aircraftradar.data.impl.flightdetails.Trail
import com.example.there.aircraftradar.data.impl.flights.Flight
import com.google.android.gms.maps.model.*

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

fun GoogleMap.loadMapStyle(context: Context, resourceId: Int) {
    try {
        val success = setMapStyle(MapStyleOptions.loadRawResourceStyle(context, resourceId))
        if (!success) {
            Log.e("ERROR", "Style parsing failed.")
        }
    } catch (e: Resources.NotFoundException) {
        Log.e("ERROR", "Can't find style. Error: ", e)
    }
}

fun GoogleMap.initUiSettings() {
    with(uiSettings) {
        isCompassEnabled = true
        isMapToolbarEnabled = false
        isIndoorLevelPickerEnabled = false
        isMyLocationButtonEnabled = false
    }
}

fun GoogleMap.addRoutePolyline(positions: List<LatLng>) {
    addPolyline(PolylineOptions()
            .addAll(positions)
            .color(Color.YELLOW)
            .clickable(true))
}