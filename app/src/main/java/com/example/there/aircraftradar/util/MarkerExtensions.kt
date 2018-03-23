package com.example.there.aircraftradar.util

import android.os.Handler
import android.os.SystemClock
import android.view.animation.LinearInterpolator
import com.google.android.gms.maps.Projection
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import android.animation.ValueAnimator
import android.view.animation.DecelerateInterpolator


fun Marker.animateToPosition(position: LatLng, hideMarker: Boolean, projection: Projection) {
    val handler = Handler()
    val start = SystemClock.uptimeMillis()
    val startPoint = projection.toScreenLocation(position)
    val startLatLng = projection.fromScreenLocation(startPoint)
    val duration: Long = 500

    val interpolator = LinearInterpolator()

    handler.post(object : Runnable {
        override fun run() {
            val elapsed = SystemClock.uptimeMillis() - start
            val t = interpolator.getInterpolation(elapsed.toFloat() / duration)
            val lng = t * position.longitude + (1 - t) * startLatLng.longitude
            val lat = t * position.latitude + (1 - t) * startLatLng.latitude
            this@animateToPosition.position = LatLng(lat, lng)

            if (t < 1.0) {
                handler.postDelayed(this, 16) // Post again 16ms later.
            } else {
                isVisible = !hideMarker
            }
        }
    })
}

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