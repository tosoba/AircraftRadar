package com.example.flightdetails.ui.fragment.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.androidmapsextensions.GoogleMap
import com.androidmapsextensions.Marker
import com.androidmapsextensions.MarkerOptions
import com.example.coreandroid.ext.*
import com.example.coreandroid.model.Flight
import com.example.coreandroid.model.FlightDetails
import com.example.coreandroid.view.ScrollViewMapFragment
import com.example.flightdetails.R
import com.example.flightdetails.ui.fragment.FlightDetailsFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLngBounds


class FlightDetailsMapFragment : FlightDetailsFragment() {

    private lateinit var map: GoogleMap
    private var flightMarker: Marker? = null
    private var routeBounds: LatLngBounds? = null

    var onMapTouch: (() -> Unit)? = null
        set(value) {
            if (field != null) return
            field = value
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_flight_details_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initMap()
    }

    private fun initMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.flight_details_map_fragment) as ScrollViewMapFragment
        mapFragment.onTouch = onMapTouch
        mapFragment.getExtendedMapAsync {
            map = it
            it.loadMapStyle(this@FlightDetailsMapFragment.context!!, R.raw.map_style)
            it.initUiSettings()
            it.setOnMapClickListener { routeBounds?.let { moveCameraToRouteBounds() } }
            setupObservers()
        }
    }

    private fun setupObservers() {
        flightDetails.observeNonNulls(this, ::displayRoute)
    }

    private fun displayRoute(flightDetails: FlightDetails) {
        if (::map.isInitialized && flightDetails.airport != null) {
            val origin = flightDetails.airport!!.origin
            val destination = flightDetails.airport!!.destination

            flightMarker = map.addFlight(flight)
            if (origin == null || destination == null) {
                moveCameraToFlight()
                Toast.makeText(context, R.string.no_airport_info, Toast.LENGTH_SHORT).show()
                return
            }

            map.addMarker(MarkerOptions().position(origin.latLng).title(origin.name))
            map.addMarker(MarkerOptions().position(destination.latLng).title(destination.name))

            routeBounds = LatLngBounds.builder().makeBounds(origin.latLng!!, flight.position, destination.latLng!!)

            moveCameraToRouteBounds()
        }
    }

    private fun moveCameraToRouteBounds() {
        map.moveCamera(CameraUpdateFactory.newLatLngBounds(routeBounds, 300))
    }

    private fun moveCameraToFlight() {
        map.moveCamera(CameraUpdateFactory.newLatLng(flight.position))
    }

    companion object {
        fun newInstance(
                flight: Flight
        ): FlightDetailsMapFragment = FlightDetailsMapFragment().apply { putArguments(flight) }
    }
}
