package com.example.there.aircraftradar

import com.example.there.aircraftradar.di.AppInjector
import com.example.there.aircraftradar.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class AircraftRadarApp : DaggerApplication() {
    override fun onCreate() {
        super.onCreate()
        AppInjector.init(this)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent
            .builder()
            .application(this)
            .build()
    }
}