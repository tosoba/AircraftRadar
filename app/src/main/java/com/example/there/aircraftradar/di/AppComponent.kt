package com.example.there.aircraftradar.di

import android.app.Application
import com.example.there.aircraftradar.AircraftRadarApp
import com.example.there.aircraftradar.di.module.AppModule
import com.example.there.aircraftradar.di.module.BuildersModule
import com.example.there.aircraftradar.di.module.DataModule
import com.example.there.aircraftradar.di.module.PresentationModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    AppModule::class,
    BuildersModule::class,
    DataModule::class,
    PresentationModule::class
])
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(app: AircraftRadarApp)
}