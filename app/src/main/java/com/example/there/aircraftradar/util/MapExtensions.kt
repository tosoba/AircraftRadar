package com.example.there.aircraftradar.util

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLngBounds


val GoogleMap.bounds: LatLngBounds
    get() = projection.visibleRegion.latLngBounds