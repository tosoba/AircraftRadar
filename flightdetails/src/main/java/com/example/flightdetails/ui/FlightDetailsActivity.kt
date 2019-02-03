package com.example.flightdetails.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.AppCompatActivity
import com.example.core.di.Injectable
import com.example.coreandroid.di.ViewModelFactory
import com.example.coreandroid.ext.hideView
import com.example.coreandroid.ext.setDrawable
import com.example.coreandroid.lifecycle.ConnectivityComponent
import com.example.coreandroid.model.Flight
import com.example.coreandroid.model.FlightDetails
import com.example.flightdetails.R
import com.example.flightdetails.ui.fragment.FlightDetailsCurrentFragment
import com.example.flightdetails.ui.fragment.info.FlightDetailsInfoFragment
import com.example.flightdetails.ui.fragment.map.FlightDetailsMapFragment
import com.shopify.livedataktx.distinct
import com.shopify.livedataktx.filter
import com.shopify.livedataktx.map
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_flight_details.*
import javax.inject.Inject


class FlightDetailsActivity : AppCompatActivity(), Injectable, HasSupportFragmentInjector, FlightDetailsContract.View {

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentDispatchingAndroidInjector

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: FlightDetailsContract.ViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(FlightDetailsViewModel::class.java)
    }

    private var currentFragment: FlightDetailsCurrentFragment = FlightDetailsCurrentFragment.INFO

    private val flight: Flight by lazy { intent.getParcelableExtra(EXTRA_FLIGHT) as Flight }

    private val connectivityComponent by lazy {
        ConnectivityComponent(this, viewModel.flightDetailsLoaded, flight_details_root_layout, ConnectivityComponent.ReloadsData {
            viewModel.loadFlightDetails(flight.id)
        })
    }

    override val flightDetails: LiveData<FlightDetails?>
        get() = viewModel.state.map { it?.flightDetails }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flight_details)

        lifecycle.addObserver(connectivityComponent)

        initFromSavedState(savedInstanceState)
        showFragment(savedInstanceState)

        initViews()

        setupObservers()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putSerializable(KEY_CURRENT_FRAGMENT, currentFragment)
    }

    private fun initFromSavedState(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            currentFragment = savedInstanceState.getSerializable(KEY_CURRENT_FRAGMENT) as FlightDetailsCurrentFragment
            flight_details_loading_progress_bar?.hideView()
        } else {
            viewModel.loadFlightDetails(flight.id)
        }
    }

    private fun showMapFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(
                R.id.flight_details_fragment_container,
                FlightDetailsMapFragment.newInstance(flight).apply {
                    onMapTouch = {
                        findViewById<NestedScrollView>(R.id.flight_details_scrollview)?.requestDisallowInterceptTouchEvent(true)
                    }
                },
                TAG_FLIGHT_DETAILS_FRAGMENT
        )
        currentFragment = FlightDetailsCurrentFragment.MAP
        flight_details_toggle_fab.setDrawable(R.drawable.info)
        transaction.commit()
    }

    private fun showInfoFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(
                R.id.flight_details_fragment_container,
                FlightDetailsInfoFragment.newInstance(flight),
                TAG_FLIGHT_DETAILS_FRAGMENT
        )
        flight_details_toggle_fab.setDrawable(R.drawable.map)
        currentFragment = FlightDetailsCurrentFragment.INFO
        transaction.commit()
    }

    private fun showFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            showMapFragment()
        } else {
            when (currentFragment) {
                FlightDetailsCurrentFragment.INFO -> showInfoFragment()
                FlightDetailsCurrentFragment.MAP -> showMapFragment()
            }
        }
    }

    private fun showFragment() {
        when (supportFragmentManager.findFragmentByTag(TAG_FLIGHT_DETAILS_FRAGMENT)) {
            is FlightDetailsInfoFragment -> showMapFragment()
            is FlightDetailsMapFragment -> showInfoFragment()
        }
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
        flight_details_toggle_fab.setOnClickListener { showFragment() }
    }

    private fun setupObservers() {
        viewModel.state.filter { it?.flightDetails != null }
                .distinct()
                .observe(this, Observer { state ->
                    state?.flightDetails?.let {
                        flight_details_loading_progress_bar?.hideView()
                        updateViews(it)
                    }
                })
    }

    private fun updateViews(flightDetails: FlightDetails) {
        flight_details_toolbar_layout?.title = flight.callsign
        flight_details_aircraft_image_view.setImageResource(R.drawable.aircraft_image_placeholder)
        if (flightDetails.imageUrl != null) {
            Picasso.get()
                    .load(flightDetails.imageUrl)
                    .placeholder(R.drawable.aircraft_image_placeholder)
                    .error(R.drawable.aircraft_image_placeholder)
                    .into(flight_details_aircraft_image_view, object : Callback {
                        override fun onError(e: Exception?) = onNoAircraftImageAvailable()
                        override fun onSuccess() = Unit
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

    companion object {
        private const val KEY_CURRENT_FRAGMENT = "KEY_CURRENT_FRAGMENT"

        const val EXTRA_FLIGHT = "EXTRA_FLIGHT"

        private const val TAG_FLIGHT_DETAILS_FRAGMENT = "TAG_FLIGHT_DETAILS_FRAGMENT"
    }
}