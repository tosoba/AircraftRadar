package com.example.there.aircraftradar.util

import android.util.Log

fun tryRun(action: () -> Unit) {
    try {
        action()
    } catch (e: Throwable) {
        Log.e("tryRun", "Error running action.")
    }
}