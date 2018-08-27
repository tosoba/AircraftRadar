package com.example.there.aircraftradar.di.module

import android.app.Application
import android.content.Context
import com.example.there.aircraftradar.data.FlightRadarApiService
import com.example.there.aircraftradar.domain.BaseFlightsRepository
import com.example.there.aircraftradar.data.FlightsRepository
import com.example.there.aircraftradar.flightdetails.FlightDetailsContract
import com.example.there.aircraftradar.flightdetails.FlightDetailsInteractor
import com.example.there.aircraftradar.map.MapContract
import com.example.there.aircraftradar.map.MapInteractor
import dagger.Binds
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

@Module
abstract class AppModule {

    @Binds
    abstract fun bindContext(application: Application): Context
}