package com.example.there.aircraftradar.map

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.os.CountDownTimer
import android.support.design.widget.BottomSheetDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.WindowManager
import com.example.there.aircraftradar.R
import com.example.there.aircraftradar.data.impl.flights.Flight
import com.example.there.aircraftradar.flightdetails.FlightDetailsActivity
import com.example.there.aircraftradar.map.cluster.MapClusterItem
import com.example.there.aircraftradar.map.cluster.MapClusterRenderer
import com.example.there.aircraftradar.util.extension.animate
import com.example.there.aircraftradar.util.extension.bounds
import com.example.there.aircraftradar.util.extension.hideView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.clustering.ClusterManager
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_map.*
import kotlinx.android.synthetic.main.flight_details_dialog.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import javax.inject.Inject


class MapActivity : AppCompatActivity() {
    private lateinit var map: GoogleMap
    private var previousZoom: Float? = null
    private val onCameraIdleListener: GoogleMap.OnCameraIdleListener by lazy {
        GoogleMap.OnCameraIdleListener {
            val currentZoom = map.cameraPosition.zoom
            if (previousZoom == currentZoom) {
                runOnUiThread { clusterManager?.onCameraIdle() }
            } else {
                doAsync {
                    clusterManager?.onCameraIdle()
                }

                previousZoom = currentZoom
            }
        }
    }
    private val onMarkerClickListener: GoogleMap.OnMarkerClickListener by lazy {
        GoogleMap.OnMarkerClickListener { marker ->
            map.moveCamera(CameraUpdateFactory.newLatLng(marker.position))
            marker.hideInfoWindow()
            val clusterItem: MapClusterItem? = currentClusterItems.find { it.title == marker.title }
            clusterItem?.let { showFlightDetailsDialog(it.flight) }
            true
        }
    }

    private var clusterManager: ClusterManager<MapClusterItem>? = null
    private val currentClusterItems = ArrayList<MapClusterItem>()

    private lateinit var loadingTimer: CountDownTimer
    private var timeTillNextLoad: Long = 10000L
    private var initialLoadCompleted: Boolean = false

    @Inject
    lateinit var viewModelFactory: MapViewModelFactory
    private val viewModel: MapContract.ViewModel by lazy { ViewModelProviders.of(this, viewModelFactory).get(MapViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        initLoadingState(savedInstanceState)

        initMap(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putParcelableArray(KEY_CURRENT_CLUSTER_ITEMS, currentClusterItems.toTypedArray())
        outState?.putLong(KEY_TIME_TILL_NEXT_LOAD, timeTillNextLoad)
        outState?.putBoolean(KEY_INITIAL_LOAD_COMPLETED, initialLoadCompleted)
    }

    private fun initLoadingState(savedInstanceState: Bundle?) {
        timeTillNextLoad = savedInstanceState?.getLong(KEY_TIME_TILL_NEXT_LOAD) ?: 10000L
        initialLoadCompleted = savedInstanceState?.getBoolean(KEY_INITIAL_LOAD_COMPLETED) ?: false
        if (initialLoadCompleted) loading_progress_bar.hideView()
    }

    private fun initMap(savedInstanceState: Bundle?) {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync {
            setupObservers()

            map = it
            previousZoom = map.cameraPosition.zoom
            loadMapStyle()

            initClusterManager()

            map.setOnCameraIdleListener(onCameraIdleListener)
            map.setOnMarkerClickListener(onMarkerClickListener)

            initLoadingTimer()

            initClusterItems(savedInstanceState)
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

    private fun loadMapStyle() {
        try {
            val success = map.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e("ERROR", "Style parsing failed.")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e("ERROR", "Can't find style. Error: ", e)
        }
    }

    private fun initClusterManager() {
        clusterManager = ClusterManager(this, map)
        clusterManager?.setAnimation(false)
        clusterManager?.renderer = MapClusterRenderer(this, map, clusterManager!!, BitmapDescriptorFactory.fromResource(R.drawable.plane))
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

    private fun initClusterItems(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(KEY_CURRENT_CLUSTER_ITEMS)) {
                val savedClusterItems = savedInstanceState.getParcelableArray(KEY_CURRENT_CLUSTER_ITEMS).map { it as MapClusterItem }
                currentClusterItems.addAll(savedClusterItems)
                clusterManager?.addItems(savedClusterItems)
            }
        } else {
            viewModel.loadFlightsInBounds(null)
        }
    }

    private fun addFlights(flights: List<Flight>) {
        clusterManager?.let {
            val markersToAnimate = clusterManager?.markerCollection?.markers?.filter {
                map.bounds.contains(it.position) && !(clusterManager?.renderer as MapClusterRenderer).isMarkerClustered(it)
            }
            val markersToAnimateTitles = markersToAnimate?.map { it.title }

            val markersToHide = clusterManager?.markerCollection?.markers?.filter {
                !map.bounds.contains(it.position) && !(clusterManager?.renderer as MapClusterRenderer).isMarkerClustered(it)
            }
            markersToHide?.forEach { it.setIcon(transitionIcon) }

            val (flightsToAdd, flightsRest) = flights.partition { markersToAnimateTitles?.contains(it.flight) == false }
            markersToAnimate?.forEach { marker ->
                val flight = flightsRest.find { it.flight == marker.title }
                flight?.let {
                    marker.animate(LatLng(flight.latitude, flight.longitude))
                }
            }

            doAsync {
                val (_, clusterItemsToRemove) = currentClusterItems.partition {
                    markersToAnimateTitles?.contains(it.flight.flight) ?: false
                }

                val clustersItemsToAdd = flightsToAdd.map { MapClusterItem(it) }
                val clusterItemsRest = flightsRest.map { MapClusterItem(it) }
                clusterItemsToRemove.forEach { clusterManager?.removeItem(it) }
                clusterManager?.addItems(clustersItemsToAdd)

                currentClusterItems.clear()
                currentClusterItems.addAll(clustersItemsToAdd.union(clusterItemsRest).toList())
                if (!initialLoadCompleted) {
                    uiThread { clusterManager?.cluster() }
                    initialLoadCompleted = true
                } else {
                    clusterManager?.cluster()
                }
            }
        }
    }

    private fun showFlightDetailsDialog(flight: Flight) {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.flight_details_dialog, null)
        with(view) {
            dialog_callsign_txt.text = flight.callsign
            dialog_position_txt.text = "${flight.latitude}, ${flight.longitude}"
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
        private const val KEY_CURRENT_CLUSTER_ITEMS = "KEY_CURRENT_CLUSTER_ITEMS"
        private const val KEY_TIME_TILL_NEXT_LOAD = "KEY_TIME_TILL_NEXT_LOAD"
        private const val KEY_INITIAL_LOAD_COMPLETED = "KEY_INITIAL_LOAD_COMPLETED"

        private const val DEFAULT_TIME_TILL_NEXT_LOAD = 10000L

        private val transitionIcon: BitmapDescriptor by lazy { BitmapDescriptorFactory.fromResource(R.drawable.transition) }
    }
}
