package com.example.there.aircraftradar.flightdetails

import dagger.Module
import dagger.Provides

@Module
class FlightDetailsModule {
    @Provides
    fun flightDetailsViewModelFactory(interactor: FlightDetailsContract.Interactor) = FlightDetailsViewModelFactory(interactor)
}