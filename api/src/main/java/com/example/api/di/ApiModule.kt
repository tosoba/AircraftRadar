package com.example.api.di

import com.example.api.FlightRadarApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

@Module
abstract class ApiModule {
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
        fun flightRadarApiService(
                retrofit: Retrofit
        ): FlightRadarApi = retrofit.create(FlightRadarApi::class.java)
    }
}