package com.example.there.aircraftradar.flightdetails

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.AppCompatActivity
import com.example.there.aircraftradar.R
import com.example.there.aircraftradar.data.model.Flight
import com.example.there.aircraftradar.data.model.FlightDetails
import com.example.there.aircraftradar.di.vm.ViewModelFactory
import com.example.there.aircraftradar.flightdetails.fragment.FlightDetailsCurrentFragment
import com.example.there.aircraftradar.flightdetails.fragment.FlightDetailsFragment
import com.example.there.aircraftradar.flightdetails.fragment.info.FlightDetailsInfoFragment
import com.example.there.aircraftradar.flightdetails.fragment.map.FlightDetailsMapFragment
import com.example.there.aircraftradar.lifecycle.ConnectivityComponent
import com.example.there.aircraftradar.util.extension.hideView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_flight_details.*
import javax.inject.Inject

class FlightDetailsActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: FlightDetailsViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(FlightDetailsViewModel::class.java)
    }

    private var currentFragment: FlightDetailsCurrentFragment = FlightDetailsCurrentFragment.INFO

    private val flight: Flight by lazy { intent.getParcelableExtra(EXTRA_FLIGHT) as Flight }
    private var flightDetails: FlightDetails? = null

    private val connectivityComponent by lazy {
        ConnectivityComponent(this, flightDetails != null, flight_details_root_layout, ConnectivityComponent.ReloadsData {
            viewModel.loadFlightDetails(flight.id)
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flight_details)

        lifecycle.addObserver(connectivityComponent)

        initFromSavedState(savedInstanceState)
        showFragment()

        initViews()

        setupObservers()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.putSerializable(KEY_CURRENT_FRAGMENT, currentFragment)
        outState?.putParcelable(KEY_FLIGHT_DETAILS, flightDetails)
    }

    private fun initFromSavedState(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            currentFragment = savedInstanceState.getSerializable(KEY_CURRENT_FRAGMENT) as FlightDetailsCurrentFragment
            flightDetails = savedInstanceState.getParcelable(KEY_FLIGHT_DETAILS) as FlightDetails
            flight_details_loading_progress_bar?.hideView()
        } else {
            viewModel.loadFlightDetails(flight.id)
        }
    }

    private fun showFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        val showingFragment = supportFragmentManager.findFragmentByTag(TAG_FLIGHT_DETAILS_FRAGMENT)
        when (currentFragment) {
            FlightDetailsCurrentFragment.INFO -> {
                if (showingFragment is FlightDetailsInfoFragment) return
                transaction.replace(R.id.flight_details_fragment_container,
                        FlightDetailsInfoFragment.newInstance(flight, flightDetails), TAG_FLIGHT_DETAILS_FRAGMENT)
            }
            FlightDetailsCurrentFragment.MAP -> {
                if (showingFragment is FlightDetailsMapFragment) return
                transaction.replace(R.id.flight_details_fragment_container,
                        FlightDetailsMapFragment.newInstance(flight, flightDetails).apply {
                            onMapTouch = {
                                findViewById<NestedScrollView>(R.id.flight_details_scrollview)?.requestDisallowInterceptTouchEvent(true)
                            }
                        }, TAG_FLIGHT_DETAILS_FRAGMENT)
            }
        }
        transaction.commit()
    }

    private fun initViews() {
        initToolbar()
        initToggleFragmentButton()
    }

    private fun initToolbar() {
        setSupportActionBar(flight_details_toolbar)
        flight_details_toolbar?.setNavigationIcon(R.drawable.arrow_back)
        flight_details_toolbar?.setNavigationOnClickListener { onBackPressed() }
    }

    private fun initToggleFragmentButton() {
        updateToggleFragmentButtonIcon()
        flight_details_toggle_fab.setOnClickListener {
            if (currentFragment == FlightDetailsCurrentFragment.INFO) {
                currentFragment = FlightDetailsCurrentFragment.MAP
                flight_details_toggle_fab.setImageResource(R.drawable.info)
            } else {
                currentFragment = FlightDetailsCurrentFragment.INFO
                flight_details_toggle_fab.setImageResource(R.drawable.map)
            }
            updateToggleFragmentButtonIcon()
            showFragment()
        }
    }

    private fun updateToggleFragmentButtonIcon() {
        if (currentFragment == FlightDetailsCurrentFragment.INFO) {
            flight_details_toggle_fab.setImageResource(R.drawable.map)
        } else {
            flight_details_toggle_fab.setImageResource(R.drawable.info)
        }
    }

    private fun setupObservers() {
        viewModel.flightDetailsResponse.observe(this, Observer { details ->
            details?.let {
                flight_details_loading_progress_bar?.hideView()
                flightDetails = it
                updateViews(it)
                updateFragment(it)
            }
        })
    }

    private fun updateViews(flightDetails: FlightDetails) {
        flight_details_toolbar_layout?.title = flight.callsign
        flight_details_aircraft_image_view.setImageResource(R.drawable.aircraft_image_placeholder)
        if (flightDetails.imageUrl != null) {
            Picasso.with(this)
                    .load(flightDetails.imageUrl)
                    .placeholder(R.drawable.aircraft_image_placeholder)
                    .error(R.drawable.aircraft_image_placeholder)
                    .into(flight_details_aircraft_image_view, object : Callback {
                        override fun onSuccess() = Unit
                        override fun onError() = onNoAircraftImageAvailable()
                    })
        } else {
            onNoAircraftImageAvailable()
        }
    }

    private fun onNoAircraftImageAvailable() {
        flight_details_aircraft_image_view.setImageResource(R.drawable.aircraft_image_placeholder)
        gradient_top.hideView()
        gradient_bottom.hideView()
    }

    private fun updateFragment(flightDetails: FlightDetails) {
        val showingFragment = supportFragmentManager.findFragmentByTag(TAG_FLIGHT_DETAILS_FRAGMENT)
        showingFragment?.let { (it as FlightDetailsFragment).flightDetails = flightDetails }
    }

    companion object {
        private const val KEY_CURRENT_FRAGMENT = "KEY_CURRENT_FRAGMENT"
        private const val KEY_FLIGHT_DETAILS = "KEY_FLIGHT_DETAILS"

        const val EXTRA_FLIGHT = "EXTRA_FLIGHT"

        private const val TAG_FLIGHT_DETAILS_FRAGMENT = "TAG_FLIGHT_DETAILS_FRAGMENT"
    }
}
