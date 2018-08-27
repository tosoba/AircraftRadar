package com.example.there.aircraftradar.map

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.support.design.widget.BottomSheetDialog
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.androidmapsextensions.*
import com.example.there.aircraftradar.R
import com.example.there.aircraftradar.data.model.Flight
import com.example.there.aircraftradar.di.vm.ViewModelFactory
import com.example.there.aircraftradar.flightdetails.FlightDetailsActivity
import com.example.there.aircraftradar.map.flight.FlightClusterOptionsProvider
import com.example.there.aircraftradar.map.flight.FlightInfoDialogAdapter
import com.example.there.aircraftradar.map.flight.FlightMarker
import com.example.there.aircraftradar.util.extension.*
import com.example.there.aircraftradar.util.tryRun
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLngBounds
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_map.*
import kotlinx.android.synthetic.main.flight_details_dialog.view.*
import javax.inject.Inject


class MapActivity : AppCompatActivity() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        initLoadingState(savedInstanceState)

        initMap(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putParcelableArray(KEY_CURRENT_FLIGHTS, currentFlightMarkers.map { it.value.flight }.toTypedArray())
        outState?.putLong(KEY_TIME_TILL_NEXT_LOAD, timeTillNextLoad)
        outState?.putBoolean(KEY_INITIAL_LOAD_COMPLETED, initialLoadCompleted)
    }

    private fun declusterify(cluster: Marker) {
        clusterifyMarkers()
        declusterifiedMarkers.addAll(cluster.markers)
        declusterifiedMarkers.forEach {
            tryRun {
                it?.clusterGroup = ClusterGroup.NOT_CLUSTERED
            }
        }
    }

    private fun clusterifyMarkers() {
        if (declusterifiedMarkers.isNotEmpty()) {
            declusterifiedMarkers.forEach {
                tryRun {
                    it?.clusterGroup = ClusterGroup.DEFAULT
                }
            }
            declusterifiedMarkers.clear()
        }
    }

    private fun initMap(savedInstanceState: Bundle?) {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.radar_map_fragment) as SupportMapFragment
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
        if (savedInstanceState != null) {
            if (!savedInstanceState.getBoolean(KEY_INITIAL_LOAD_COMPLETED)) return

            map.clear()
            if (savedInstanceState.containsKey(KEY_CURRENT_FLIGHTS)) {
                val savedClusterItems = savedInstanceState.getParcelableArray(KEY_CURRENT_FLIGHTS).map { it as Flight }
                savedClusterItems.forEach {
                    val marker = map.addFlight(it)
                    marker.clusterGroup = ClusterGroup.DEFAULT
                    currentFlightMarkers[it.callsign] = FlightMarker(it, marker)
                }
            }
        } else {
            viewModel.loadFlightsInBounds(null)
        }
    }

    private fun setupObservers() {
        viewModel.flightsResponse.observe(this, Observer { flights ->
            flights?.let {
                addFlights(it)
                loading_progress_bar.hideView()
            }
        })
    }

    private fun initLoadingTimer() {
        loadingTimer = object : CountDownTimer(timeTillNextLoad, 500) {
            override fun onFinish() {
                viewModel.loadFlightsInBounds(null)
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
        val bounds = map.bounds
        updatedFlights.forEach { flight ->
            if (currentFlightMarkers.containsKey(flight.callsign)) {
                currentFlightMarkers[flight.callsign]?.flight = flight
                currentFlightMarkers[flight.callsign]?.marker?.let {
                    if (bounds.contains(it.position) && !it.isCluster) it.animatePosition(flight.position)
                    else it.animatePosition(flight.position, AnimationSettings().duration(1L))
                }
            } else {
                val marker = map.addFlight(flight)
                currentFlightMarkers[flight.callsign] = FlightMarker(flight, marker)
            }
        }
        map.setClustering(clusteringSettings)
    }

    private fun showFlightDetailsDialog(flight: Flight) {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.flight_details_dialog, null)
        with(view) {
            flight_info_dialog_recycler_view.setLayoutManager(this@MapActivity, screenOrientation, 2)
            flight_info_dialog_recycler_view.adapter = FlightInfoDialogAdapter(flight.info)
            dialog_more_btn.setOnClickListener {
                dialog.dismiss()
                startFlightDetailsActivity(flight)
            }
        }
        dialog.window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialog.setContentView(view)
        dialog.show()
    }

    private fun startFlightDetailsActivity(flight: Flight) {
        val intent = Intent(this, FlightDetailsActivity::class.java)
        intent.putExtra(FlightDetailsActivity.EXTRA_FLIGHT, flight)
        startActivity(intent)
    }

    companion object {
        private const val KEY_CURRENT_FLIGHTS = "KEY_CURRENT_FLIGHTS"
        private const val KEY_TIME_TILL_NEXT_LOAD = "KEY_TIME_TILL_NEXT_LOAD"
        private const val KEY_INITIAL_LOAD_COMPLETED = "KEY_INITIAL_LOAD_COMPLETED"

        private const val DEFAULT_TIME_TILL_NEXT_LOAD = 10000L
    }
}
