package com.example.there.aircraftradar.di.module

import com.example.coreandroid.coroutine.CoroutineContextProvider
import com.example.coreandroid.coroutine.CoroutineContextProviderImpl
import dagger.Binds
import dagger.Module

@Module
abstract class CoroutineModule {
    @Binds
    abstract fun coroutineContextProvider(provider: CoroutineContextProviderImpl): CoroutineContextProvider
}