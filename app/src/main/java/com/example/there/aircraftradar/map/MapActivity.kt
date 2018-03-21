package com.example.there.aircraftradar.map

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.WindowManager
import com.example.there.aircraftradar.R
import com.example.there.aircraftradar.data.impl.flights.Flight
import com.example.there.aircraftradar.flightdetails.FlightDetailsActivity
import com.example.there.aircraftradar.util.rotateBitmap
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.flight_details_dialog.view.*
import net.sharewire.googlemapsclustering.*
import org.jetbrains.anko.doAsync
import javax.inject.Inject


class MapActivity : AppCompatActivity() {

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
            Log.e("ZOOM", map.maxZoomLevel.toString())

            initClusterManager()

            map.setOnCameraIdleListener {
                val bounds = map.projection.visibleRegion.latLngBounds
                viewModel.loadFlightsInBounds(bounds)
                clusterManager?.onCameraIdle()
                removeFlightsOutsideBounds(bounds)
            }


            if (savedInstanceState != null) {
                if (savedInstanceState.containsKey(KEY_CURRENT_CLUSTER_ITEMS)) {
                    val savedClusterItems = savedInstanceState.getParcelableArray(KEY_CURRENT_CLUSTER_ITEMS).map { it as MapClusterItem }
                    currentClusterItems.addAll(savedClusterItems)
                    clusterManager?.setItems(savedClusterItems)
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

    private fun initClusterManager() {
        clusterManager = ClusterManager(this, map)
        clusterManager?.setMinClusterSize(5)
        clusterManager?.setIconGenerator(object : IconGenerator<MapClusterItem> {
            override fun getClusterIcon(cluster: Cluster<MapClusterItem>): BitmapDescriptor =
                    DefaultIconGenerator<MapClusterItem>(this@MapActivity).getClusterIcon(cluster)

            override fun getClusterItemIcon(clusterItem: MapClusterItem): BitmapDescriptor =
                    BitmapDescriptorFactory.fromBitmap(rotateBitmap(BitmapFactory.decodeResource(resources, R.drawable.plane),
                            clusterItem.flight.bearing.toFloat()))
        })
        clusterManager?.setCallbacks(object : ClusterManager.Callbacks<MapClusterItem> {
            override fun onClusterClick(cluster: Cluster<MapClusterItem>): Boolean {
                val currentZoom = map.cameraPosition.zoom
                if (currentZoom + 1 < map.maxZoomLevel) {
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(cluster.latitude, cluster.longitude), currentZoom + 1.0f))
                } else {
                    map.moveCamera(CameraUpdateFactory.newLatLng(LatLng(cluster.latitude, cluster.longitude)))
                }
                return true
            }

            override fun onClusterItemClick(clusterItem: MapClusterItem): Boolean {
                showFlightDetailsDialog(clusterItem.flight)
                return true
            }
        })
    }

    private fun addFlights(flights: List<Flight>) = clusterManager?.let {
        doAsync {
            val newClusterItems = flights.map { MapClusterItem(it) }
            it.setItems(flights.map { MapClusterItem(it) })
            currentClusterItems.addAll(newClusterItems)
        }
    }

    private fun removeFlightsOutsideBounds(bounds: LatLngBounds) {
        doAsync {
            val toRemove = currentClusterItems.filter { !bounds.contains(LatLng(it.latitude, it.longitude)) }
            currentClusterItems.removeAll(toRemove)
        }
    }

    private fun showFlightDetailsDialog(flight: Flight) {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.flight_details_dialog, null)
        view.dialog_flight_id_txt.text = flight.id
        view.dialog_more_btn.setOnClickListener {
            dialog.dismiss()
            startFlightDetaisActivity(flight)
        }
        dialog.window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialog.setContentView(view)
        dialog.show()
    }

    private fun startFlightDetaisActivity(flight: Flight) {
        val intent = Intent(this, FlightDetailsActivity::class.java)
        intent.putExtra(FlightDetailsActivity.EXTRA_FLIGHT, flight)
        startActivity(intent)
    }

    companion object {
        private const val KEY_CURRENT_CLUSTER_ITEMS = "KEY_CURRENT_CLUSTER_ITEMS"
    }
}
