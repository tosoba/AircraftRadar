package com.example.there.aircraftradar.di

import com.example.there.aircraftradar.map.MapActivity
import com.example.there.aircraftradar.map.MapModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BuildersModule {
    @ContributesAndroidInjector(modules = [MapModule::class])
    abstract fun bindMapActivity(): MapActivity
}