package com.example.coreandroid.routing

import androidx.appcompat.app.AppCompatActivity
import com.example.coreandroid.model.Flight

interface Router {
    fun showFlightDetails(activity: AppCompatActivity, flight: Flight)
}