package com.example.there.aircraftradar.di.module

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.coreandroid.coroutine.CoroutineContextProvider
import com.example.coreandroid.di.vm.ViewModelFactory
import com.example.coreandroid.di.vm.ViewModelKey
import com.example.coreandroid.routing.Router
import com.example.flightdetails.data.FlightDetailsRepository
import com.example.flightdetails.domain.feature.FlightDetailsFeature
import com.example.flightdetails.domain.repo.IFlightDetailsRepository
import com.example.flightdetails.ui.FlightDetailsViewModel
import com.example.map.data.MapRepository
import com.example.map.domain.feature.FlightsFeature
import com.example.map.domain.repo.IMapRepository
import com.example.map.ui.MapViewModel
import com.example.there.aircraftradar.routing.AppRouter
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
abstract class AppModule {
    @Binds
    abstract fun bindContext(application: Application): Context

    @Binds
    abstract fun flightDetailsRepository(repository: FlightDetailsRepository): IFlightDetailsRepository

    @Binds
    abstract fun mapRepository(repository: MapRepository): IMapRepository

    @Binds
    abstract fun viewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(FlightDetailsViewModel::class)
    abstract fun flightDetailsViewModel(viewModel: FlightDetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MapViewModel::class)
    abstract fun mapViewModel(viewModel: MapViewModel): ViewModel

    @Binds
    abstract fun router(appRouter: AppRouter): Router

    @Module
    companion object {
        @Provides
        @JvmStatic
        fun flightsFeature(
            coroutineContextProvider: CoroutineContextProvider,
            repository: IMapRepository
        ): FlightsFeature = FlightsFeature(coroutineContextProvider, repository)

        @Provides
        @JvmStatic
        fun flightDetailsFeature(
            coroutineContextProvider: CoroutineContextProvider,
            repository: IFlightDetailsRepository
        ): FlightDetailsFeature = FlightDetailsFeature(coroutineContextProvider, repository)
    }
}