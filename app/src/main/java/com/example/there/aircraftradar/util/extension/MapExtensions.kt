package com.example.there.aircraftradar.util.extension

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLngBounds


val GoogleMap.bounds: LatLngBounds
    get() = projection.visibleRegion.latLngBounds