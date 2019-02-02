package com.example.coreandroid.ext

import android.content.Context
import android.content.res.Configuration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View

fun View.hideView() {
    if (visibility == View.VISIBLE) visibility = View.GONE
}

fun RecyclerView.setLayoutManager(context: Context, screenOrientation: Int, columnsWhenHorizontal: Int) {
    layoutManager = if (screenOrientation == Configuration.ORIENTATION_PORTRAIT) {
        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    } else {
        GridLayoutManager(context, columnsWhenHorizontal)
    }
}