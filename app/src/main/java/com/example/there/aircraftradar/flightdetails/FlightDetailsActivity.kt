package com.example.there.aircraftradar.flightdetails

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.there.aircraftradar.R
import com.example.there.aircraftradar.data.impl.flights.Flight
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_flight_details.*
import javax.inject.Inject

class FlightDetailsActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: FlightDetailsViewModelFactory

    private val viewModel: FlightDetailsViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(FlightDetailsViewModel::class.java)
    }

    private val flight: Flight by lazy { intent.getParcelableExtra(EXTRA_FLIGHT) as Flight }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flight_details)

        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupObservers()
        viewModel.loadFlightDetails(flight.id)
    }

    private fun setupObservers() {
        viewModel.flightDetailsResponse.observe(this, Observer {
            it?.let {
                Log.e("fd", it.aircraft.model.code)
            }
        })
    }

    companion object {
        const val EXTRA_FLIGHT = "EXTRA_FLIGHT"
    }
}
