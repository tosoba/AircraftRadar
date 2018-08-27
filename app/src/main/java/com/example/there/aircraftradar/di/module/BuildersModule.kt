package com.example.there.aircraftradar.di.module

import com.example.there.aircraftradar.flightdetails.FlightDetailsActivity
import com.example.there.aircraftradar.map.MapActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BuildersModule {

    @ContributesAndroidInjector
    abstract fun bindMapActivity(): MapActivity

    @ContributesAndroidInjector
    abstract fun bindFlightDetailsActivity(): FlightDetailsActivity
}