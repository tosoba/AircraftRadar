package com.example.coreandroid.ext

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer

fun <T : Any> LiveData<T?>.observeIgnoringNulls(
        lifecycleOwner: LifecycleOwner,
        onNext: (T) -> Unit
) {
    observe(lifecycleOwner, Observer {
        it?.let(onNext)
    })
}