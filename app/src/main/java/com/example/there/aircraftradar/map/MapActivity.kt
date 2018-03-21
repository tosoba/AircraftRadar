package com.example.there.aircraftradar.map

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.there.aircraftradar.R
import com.example.there.aircraftradar.data.impl.flights.Flight
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.clustering.ClusterManager
import dagger.android.AndroidInjection
import org.jetbrains.anko.doAsync
import javax.inject.Inject


class MapActivity : AppCompatActivity(), MapContract.Routing {

    private lateinit var map: GoogleMap
    private var clusterManager: ClusterManager<MapClusterItem>? = null
    private val currentClusterItems: MutableSet<MapClusterItem> = mutableSetOf()

    @Inject
    lateinit var viewModelFactory: MapViewModelFactory

    private val viewModel: MapViewModel by lazy { ViewModelProviders.of(this, viewModelFactory).get(MapViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        initMap(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putParcelableArray(KEY_CURRENT_CLUSTER_ITEMS, currentClusterItems.toTypedArray())
    }

    private fun initMap(savedInstanceState: Bundle?) {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync {
            setupObservers()
            map = it

            clusterManager = ClusterManager(this, map)
            clusterManager?.setAnimation(false)

            map.setOnCameraIdleListener {
                val bounds = map.projection.visibleRegion.latLngBounds
                viewModel.loadFlightsInBounds(bounds)
                clusterManager?.onCameraIdle()
                removeFlightsOutsideBounds(bounds)
            }

            map.setOnMarkerClickListener(clusterManager)

            if (savedInstanceState != null) {
                if (savedInstanceState.containsKey(KEY_CURRENT_CLUSTER_ITEMS)) {
                    val savedClusterItems = savedInstanceState.getParcelableArray(KEY_CURRENT_CLUSTER_ITEMS).map { it as MapClusterItem }
                    currentClusterItems.addAll(savedClusterItems)
                    clusterManager?.addItems(savedClusterItems)
                }
            } else {
                viewModel.loadFlightsInBounds(map.projection.visibleRegion.latLngBounds)
            }
        }
    }

    private fun setupObservers() {
        viewModel.flightsResponse.observe(this, Observer { flights ->
            flights?.let { addFlights(it) }
        })
    }

    private fun addFlights(flights: List<Flight>) = clusterManager?.let {
        doAsync {
            val newClusterItems = flights.map { MapClusterItem(it) }
            it.addItems(newClusterItems.filter { !currentClusterItems.contains(it) })
            currentClusterItems.addAll(newClusterItems)
        }
    }

    private fun removeFlightsOutsideBounds(bounds: LatLngBounds) {
        doAsync {
            val toRemove = currentClusterItems.filter { !bounds.contains(it.position) }
            toRemove.forEach { clusterManager?.removeItem(it) }
            currentClusterItems.removeAll(toRemove)
        }
    }

    companion object {
        private const val KEY_CURRENT_CLUSTER_ITEMS = "KEY_CURRENT_CLUSTER_ITEMS"
    }
}
