package com.example.there.aircraftradar.di.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.there.aircraftradar.di.vm.ViewModelFactory
import com.example.there.aircraftradar.di.vm.ViewModelKey
import com.example.there.aircraftradar.flightdetails.FlightDetailsContract
import com.example.there.aircraftradar.flightdetails.FlightDetailsInteractor
import com.example.there.aircraftradar.flightdetails.FlightDetailsViewModel
import com.example.there.aircraftradar.map.MapContract
import com.example.there.aircraftradar.map.MapInteractor
import com.example.there.aircraftradar.map.MapViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class PresentationModule {

    @Binds
    abstract fun bindMapInteractor(interactor: MapInteractor): MapContract.Interactor

    @Binds
    abstract fun bindFlightDetailsInteractor(interactor: FlightDetailsInteractor): FlightDetailsContract.Interactor

    @Binds
    @IntoMap
    @ViewModelKey(FlightDetailsViewModel::class)
    abstract fun flightDetailsViewModel(viewModel: FlightDetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MapViewModel::class)
    abstract fun mapViewModel(viewModel: MapViewModel): ViewModel

    @Binds
    abstract fun viewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}