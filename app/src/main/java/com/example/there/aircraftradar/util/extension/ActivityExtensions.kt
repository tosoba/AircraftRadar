package com.example.there.aircraftradar.util.extension

import android.app.Activity

val Activity.screenOrientation: Int
    get() = resources.configuration.orientation