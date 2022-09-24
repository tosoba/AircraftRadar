package com.example.coreandroid.ext

import android.content.Context
import android.content.res.Resources
import android.util.Log
import com.androidmapsextensions.GoogleMap
import com.androidmapsextensions.Marker
import com.androidmapsextensions.MarkerOptions
import com.example.coreandroid.R
import com.example.coreandroid.model.Flight
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds

val LatLng.formattedString: String
    get() = "${latitude.format(2)} °${if (latitude > 0) "N" else "S"}, ${longitude.format(2)} °${if (longitude > 0) "E" else "W"}"

fun LatLngBounds.Builder.makeBounds(vararg positions: LatLng): LatLngBounds {
    positions.forEach { include(it) }
    return build()
}

private val flightIcon: BitmapDescriptor by lazy { BitmapDescriptorFactory.fromResource(R.drawable.plane) }

val GoogleMap.bounds: LatLngBounds
    get() = projection.visibleRegion.latLngBounds

fun GoogleMap.addFlight(flight: Flight): Marker = addMarker(
    MarkerOptions()
        .title(flight.callsign)
        .position(flight.position)
        .icon(flightIcon)
        .rotation(flight.bearing.toFloat())
)

fun GoogleMap.loadMapStyle(context: Context, resourceId: Int) {
    try {
        val success = setMapStyle(
            com.google.android.gms.maps.model.MapStyleOptions.loadRawResourceStyle(
                context,
                resourceId
            )
        )
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

fun List<Marker>.trySetClusterGroupEach(group: Int) {
    forEach {
        try {
            it.clusterGroup = group
        } catch (e: Exception) {
            Log.e("CLUSTER", "Error occurred", e)
        }
    }
}