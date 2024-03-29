package com.example.coreandroid.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.androidmapsextensions.SupportMapFragment

class ScrollViewMapFragment : SupportMapFragment() {
    var onTouch: (() -> Unit)? = null
        set(value) {
            if (field != null) return
            field = value
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val layout = super.onCreateView(inflater, parent, savedInstanceState)
        val frameLayout = TouchableWrapper(inflater.context)
        frameLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
        (layout as ViewGroup).addView(
            frameLayout,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
        return layout
    }

    inner class TouchableWrapper(context: Context) : FrameLayout(context) {
        override fun dispatchTouchEvent(event: MotionEvent): Boolean {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> onTouch?.invoke()
                MotionEvent.ACTION_UP -> onTouch?.invoke()
            }
            return super.dispatchTouchEvent(event)
        }
    }
}