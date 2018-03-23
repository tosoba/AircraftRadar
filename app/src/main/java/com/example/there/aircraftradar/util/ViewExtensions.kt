package com.example.there.aircraftradar.util

import android.view.View

fun View.hideView() {
    if (visibility == View.VISIBLE) visibility = View.GONE
}