package com.example.coreandroid.ext

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData

fun <T : Any> LiveData<T?>.observeNonNulls(
    lifecycleOwner: LifecycleOwner,
    onNext: (T) -> Unit
) {
    observe(lifecycleOwner) { it?.let(onNext) }
}