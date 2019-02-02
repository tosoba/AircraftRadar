package com.example.coreandroid.ext

import java.util.*

fun Double.format(digits: Int): String = String.format("%.${digits}f", this)

fun Long.toDate(): Date = Date(this * 1000)