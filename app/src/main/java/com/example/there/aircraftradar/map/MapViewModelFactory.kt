package com.example.there.aircraftradar.map

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

class MapViewModelFactory(private val interactor: MapContract.Interactor): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
            return MapViewModel(interactor) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}