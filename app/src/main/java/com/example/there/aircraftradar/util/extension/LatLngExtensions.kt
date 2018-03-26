package com.example.there.aircraftradar.util.extension

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds

val LatLng.formattedString: String
    get() = "${latitude.format(2)} °${if (latitude > 0) "N" else "S"}, ${longitude.format(2)} °${if (longitude > 0) "E" else "W"}"

fun LatLngBounds.Builder.makeBounds(vararg positions: LatLng): LatLngBounds {
    positions.forEach { include(it) }
    return build()
}