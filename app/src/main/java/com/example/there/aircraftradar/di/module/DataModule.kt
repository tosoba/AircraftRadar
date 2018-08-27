package com.example.there.aircraftradar.di.module

import com.example.there.aircraftradar.data.FlightRadarApiService
import com.example.there.aircraftradar.data.FlightsRepository
import com.example.there.aircraftradar.domain.BaseFlightsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

@Module
abstract class DataModule {

    @Binds
    abstract fun bindFlightsRepository(repository: FlightsRepository): BaseFlightsRepository

    @Module
    companion object {
        private const val baseApiUrl = "https://data-live.flightradar24.com/"

        @Provides
        @JvmStatic
        fun flightRadarApiRetrofit(): Retrofit = Retrofit.Builder()
                .baseUrl(baseApiUrl)
                .client(OkHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        @Provides
        @JvmStatic
        fun flightRadarApiService(retrofit: Retrofit): FlightRadarApiService = retrofit.create(FlightRadarApiService::class.java)
    }
}