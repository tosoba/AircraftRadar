package com.example.there.aircraftradar.map

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.there.aircraftradar.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import dagger.android.AndroidInjection
import javax.inject.Inject

class MapActivity : AppCompatActivity(), MapContract.Routing {

    private lateinit var map: GoogleMap

    @Inject
    lateinit var viewModelFactory: MapViewModelFactory

    private val viewModel: MapViewModel by lazy { ViewModelProviders.of(this, viewModelFactory).get(MapViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        initMap()
        setupObservers()
    }

    private fun initMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync {
            map = it

            map.setOnCameraIdleListener {
                val bounds = it.projection.visibleRegion.latLngBounds
                viewModel.loadFlightsInBounds(bounds)
            }
        }
    }

    private fun setupObservers() {
        viewModel.flightsResponse.observe(this, Observer { flights ->
            flights?.let {
                it.forEach { Log.e("FLIGHT", it.id) }
            }
        })
    }
}
