package com.example.there.aircraftradar.map

import dagger.Module
import dagger.Provides

@Module
class MapModule {
    @Provides
    fun mapViewModelFactory(mapInteractor: MapContract.Interactor) = MapViewModelFactory(mapInteractor)
}