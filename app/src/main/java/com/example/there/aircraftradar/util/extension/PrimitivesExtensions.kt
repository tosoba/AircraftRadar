package com.example.there.aircraftradar.util.extension

import java.util.*

fun Double.format(digits: Int): String = String.format("%.${digits}f", this)

fun Long.toDate(): Date = Date(this * 1000)