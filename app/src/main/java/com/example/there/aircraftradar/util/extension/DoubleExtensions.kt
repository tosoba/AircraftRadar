package com.example.there.aircraftradar.util.extension

fun Double.format(digits: Int): String = String.format("%.${digits}f", this)