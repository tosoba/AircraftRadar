package com.example.there.aircraftradar.routing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.example.coreandroid.model.Flight
import com.example.coreandroid.routing.Router
import com.example.flightdetails.ui.FlightDetailsActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRouter @Inject constructor() : Router {
    override fun showFlightDetails(activity: AppCompatActivity, flight: Flight) {
        val intent = Intent(activity, FlightDetailsActivity::class.java)
        intent.putExtra(FlightDetailsActivity.EXTRA_FLIGHT, flight)
        activity.startActivity(intent)
    }
}