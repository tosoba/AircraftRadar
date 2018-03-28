package com.example.there.aircraftradar.util.extension

import android.content.Context

fun Context.spToPx(sp: Int): Int {
    val scale = resources.displayMetrics.scaledDensity
    return (sp * scale + 0.5f).toInt()
}