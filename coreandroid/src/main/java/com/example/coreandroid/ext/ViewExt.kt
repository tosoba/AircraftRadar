package com.example.coreandroid.ext

import android.content.Context
import android.content.res.Configuration
import android.view.View
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

fun View.hideView() {
    if (visibility == View.VISIBLE) visibility = View.GONE
}

fun RecyclerView.setLayoutManager(
    context: Context,
    screenOrientation: Int,
    columnsWhenHorizontal: Int
) {
    layoutManager = if (screenOrientation == Configuration.ORIENTATION_PORTRAIT) {
        LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
    } else {
        GridLayoutManager(
            context,
            columnsWhenHorizontal
        )
    }
}

fun FloatingActionButton.setDrawable(@DrawableRes drawableRes: Int) {
    hide()
    setImageResource(drawableRes)
    show()
}