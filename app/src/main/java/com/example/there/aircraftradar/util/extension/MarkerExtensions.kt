package com.example.there.aircraftradar.util.extension

import android.animation.ValueAnimator
import android.view.animation.DecelerateInterpolator
import com.example.there.aircraftradar.util.DoubleArrayEvaluator
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker


fun Marker.animate(destLatLng: LatLng) {
    if (position == destLatLng) return
    val startValues = doubleArrayOf(position.latitude, position.longitude)
    val endValues = doubleArrayOf(destLatLng.latitude, destLatLng.longitude)
    val latLngAnimator = ValueAnimator.ofObject(DoubleArrayEvaluator(), startValues, endValues)
    latLngAnimator.duration = 1000
    latLngAnimator.interpolator = DecelerateInterpolator()
    latLngAnimator.addUpdateListener { animation ->
        val animatedValue = animation.animatedValue as DoubleArray
        position = LatLng(animatedValue[0], animatedValue[1])
    }
    latLngAnimator.start()
}