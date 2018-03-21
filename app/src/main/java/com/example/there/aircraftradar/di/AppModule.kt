package com.example.there.aircraftradar.di

import android.app.Application
import android.content.Context
import com.example.there.aircraftradar.data.FlightRadarApiService
import com.example.there.aircraftradar.data.base.repository.BaseFlightsRepository
import com.example.there.aircraftradar.data.impl.flights.FlightsMapper
import com.example.there.aircraftradar.data.impl.flights.FlightsRepository
import com.example.there.aircraftradar.map.MapContract
import com.example.there.aircraftradar.map.MapInteractor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@Module
class AppModule(private val app: Application) {
    private val baseApiUrl = "https://data-live.flightradar24.com/"

    @Provides
    @Singleton
    fun application(): Application = app

    @Provides
    @Singleton
    fun provideContext(application: Application): Context = application.applicationContext

    @Provides
    @Singleton
    fun flightRadarApiRetrofit(): Retrofit = Retrofit.Builder()
            .baseUrl(baseApiUrl)
            .client(OkHttpClient())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun flightRadarApiService(retrofit: Retrofit): FlightRadarApiService = retrofit.create(FlightRadarApiService::class.java)

    @Provides
    @Singleton
    fun flightsMapper(): FlightsMapper = FlightsMapper()

    @Provides
    @Singleton
    fun flightsRepository(apiService: FlightRadarApiService,
                          mapper: FlightsMapper): BaseFlightsRepository = FlightsRepository(apiService, mapper)

    @Provides
    @Singleton
    fun mapInteractor(flightsRepository: BaseFlightsRepository): MapContract.Interactor = MapInteractor(flightsRepository)
}