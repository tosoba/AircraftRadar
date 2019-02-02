package com.example.coreandroid.routing

import android.support.v7.app.AppCompatActivity
import com.example.coreandroid.model.Flight

interface Router {
    fun showFlightDetails(activity: AppCompatActivity, flight: Flight)
}