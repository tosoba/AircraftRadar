package com.example.there.aircraftradar.util.extension

import android.view.View

fun View.hideView() {
    if (visibility == View.VISIBLE) visibility = View.GONE
}