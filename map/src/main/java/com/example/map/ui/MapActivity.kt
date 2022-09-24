package com.example.map.ui

import android.os.Bundle
import android.os.CountDownTimer
import android.view.WindowManager
import androidx.lifecycle.ViewModelProviders
import com.androidmapsextensions.*
import com.example.core.di.Injectable
import com.example.coreandroid.di.ViewModelFactory
import com.example.coreandroid.ext.*
import com.example.coreandroid.lifecycle.ConnectivityComponent
import com.example.coreandroid.model.Flight
import com.example.coreandroid.routing.Router
import com.example.map.R
import com.example.map.ui.flight.FlightClusterOptionsProvider
import com.example.map.ui.flight.FlightInfoDialogAdapter
import com.example.map.ui.flight.FlightMarker
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.shopify.livedataktx.filter
import com.shopify.livedataktx.map
import com.shopify.livedataktx.observe
import dagger.android.HasAndroidInjector
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_map.*
import kotlinx.android.synthetic.main.flight_details_dialog.view.*
import javax.inject.Inject

class MapActivity : DaggerAppCompatActivity(), HasAndroidInjector, Injectable {
    private lateinit var map: GoogleMap
    private val currentFlightMarkers = HashMap<String, FlightMarker>()
    private var declusterifiedMarkers = ArrayList<Marker>()
    private val clusteringSettings: ClusteringSettings by lazy {
        ClusteringSettings()
            .clusterOptionsProvider(FlightClusterOptionsProvider(resources))
            .addMarkersDynamically(true)
    }
    private val onMarkerClickListener: GoogleMap.OnMarkerClickListener by lazy {
        GoogleMap.OnMarkerClickListener { marker ->
            if (marker == null) return@OnMarkerClickListener true

            if (marker.isCluster) {
                val builder = LatLngBounds.builder()
                marker.markers.forEach { builder.include(it.position) }
                val bounds = builder.build()
                map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
                declusterify(marker)
            } else {
                val flight = currentFlightMarkers.values.find { it.marker == marker }?.flight
                flight?.let {
                    map.moveCamera(CameraUpdateFactory.newLatLng(flight.position))
                    showFlightDetailsDialog(it)
                }
            }
            return@OnMarkerClickListener true
        }
    }

    private lateinit var loadingTimer: CountDownTimer
    private var timeTillNextLoad: Long = 10000L
    private var initialLoadCompleted: Boolean = false

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: MapContract.ViewModel by lazy {

        ViewModelProviders.of(this, viewModelFactory).get(MapViewModel::class.java)
    }

    private val connectivityComponent by lazy {
        ConnectivityComponent(this, currentFlightMarkers.isNotEmpty(), map_root_layout) {
            viewModel.loadFlights()
        }
    }

    @Inject
    lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        lifecycle.addObserver(connectivityComponent)

        initLoadingState(savedInstanceState)

        initMap(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong(KEY_TIME_TILL_NEXT_LOAD, timeTillNextLoad)
        outState.putBoolean(KEY_INITIAL_LOAD_COMPLETED, initialLoadCompleted)
    }

    private fun declusterify(cluster: Marker) {
        clusterifyMarkers()
        declusterifiedMarkers.addAll(cluster.markers)
        declusterifiedMarkers.trySetClusterGroupEach(ClusterGroup.NOT_CLUSTERED)
    }

    private fun clusterifyMarkers() {
        if (declusterifiedMarkers.isNotEmpty()) {
            declusterifiedMarkers.trySetClusterGroupEach(ClusterGroup.DEFAULT)
            declusterifiedMarkers.clear()
        }
    }

    private fun initMap(savedInstanceState: Bundle?) {
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.radar_map_fragment) as SupportMapFragment
        mapFragment.getExtendedMapAsync {
            map = it
            setupObservers()
            initLoadingTimer()
            map.loadMapStyle(this, R.raw.map_style)
            map.initUiSettings()
            map.setClustering(clusteringSettings)

            map.setOnMarkerClickListener(onMarkerClickListener)
            map.setOnMapClickListener { clusterifyMarkers() }

            initClusterItems(savedInstanceState)
        }
    }

    private fun initLoadingState(savedInstanceState: Bundle?) {
        timeTillNextLoad = savedInstanceState?.getLong(KEY_TIME_TILL_NEXT_LOAD) ?: 10000L
        initialLoadCompleted = savedInstanceState?.getBoolean(KEY_INITIAL_LOAD_COMPLETED) ?: false
        if (initialLoadCompleted) loading_progress_bar.hideView()
    }

    private fun initClusterItems(savedInstanceState: Bundle?) {
        if (savedInstanceState == null)
            viewModel.loadFlights()
    }

    private fun setupObservers() {
        viewModel.state
            .map { it?.flights ?: emptyList() }
            .filter { it?.isNotEmpty() ?: false }
            .observe {
                addFlights(it!!)
                loading_progress_bar?.hideView()
            }
    }

    private fun initLoadingTimer() {
        loadingTimer = object : CountDownTimer(timeTillNextLoad, 500) {
            override fun onFinish() {
                if (connectivityComponent.lastConnectionStatus) {
                    viewModel.loadFlights()
                }
                timeTillNextLoad = DEFAULT_TIME_TILL_NEXT_LOAD
                initLoadingTimer()
            }

            override fun onTick(millisUntilFinished: Long) {
                timeTillNextLoad = millisUntilFinished
            }
        }
        loadingTimer.start()
    }

    private fun addFlights(updatedFlights: List<Flight>) {
        updatedFlights.forEach { flight ->
            currentFlightMarkers[flight.callsign]?.let {
                it.flight = flight
                if (map.bounds.contains(it.marker.position) && !it.marker.isCluster) it.marker.animatePosition(
                    flight.position
                )
                else it.marker.animatePosition(flight.position, AnimationSettings().duration(1L))
            } ?: run {
                val marker = map.addFlight(flight)
                currentFlightMarkers[flight.callsign] = FlightMarker(flight, marker)
            }
        }
        map.setClustering(clusteringSettings)
    }

    private fun showFlightDetailsDialog(flight: Flight) {
        val dialog =
            BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.flight_details_dialog, null)
        with(view) {
            flight_info_dialog_recycler_view.setLayoutManager(
                this@MapActivity,
                screenOrientation,
                2
            )
            flight_info_dialog_recycler_view.adapter = FlightInfoDialogAdapter(flight.info)
            dialog_more_btn.setOnClickListener {
                dialog.dismiss()
                startFlightDetailsActivity(flight)
            }
        }
        dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialog.setContentView(view)
        dialog.show()
    }

    private fun startFlightDetailsActivity(flight: Flight) {
        router.showFlightDetails(this, flight)
    }

    companion object {
        private const val KEY_TIME_TILL_NEXT_LOAD = "KEY_TIME_TILL_NEXT_LOAD"
        private const val KEY_INITIAL_LOAD_COMPLETED = "KEY_INITIAL_LOAD_COMPLETED"

        private const val DEFAULT_TIME_TILL_NEXT_LOAD = 10000L
    }
}