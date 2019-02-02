package com.example.there.aircraftradar.di.module

import com.example.flightdetails.ui.FlightDetailsActivity
import com.example.map.ui.MapActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BuildersModule {

    @ContributesAndroidInjector
    abstract fun bindMapActivity(): MapActivity

    @ContributesAndroidInjector
    abstract fun bindFlightDetailsActivity(): FlightDetailsActivity
}