package com.example.coreandroid.ext

import android.app.Activity
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager

fun Activity.registerFragmentLifecycleCallbacks(
    callbacks: FragmentManager.FragmentLifecycleCallbacks,
    recursive: Boolean
) = (this as? FragmentActivity)
        ?.supportFragmentManager
        ?.registerFragmentLifecycleCallbacks(callbacks, recursive)

val Activity.screenOrientation: Int
    get() = resources.configuration.orientation