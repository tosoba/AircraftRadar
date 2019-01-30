package com.example.coreandroid.ext

import com.google.android.gms.maps.model.LatLngBounds

val LatLngBounds?.apiString: String?
    get() = if (this != null)
        "${northeast.latitude},${southwest.latitude},${southwest.longitude},${northeast.longitude}"
    else null