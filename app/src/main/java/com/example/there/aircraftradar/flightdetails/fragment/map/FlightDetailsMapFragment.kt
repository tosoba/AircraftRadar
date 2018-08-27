package com.example.there.aircraftradar.flightdetails.fragment.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.androidmapsextensions.GoogleMap
import com.androidmapsextensions.Marker
import com.androidmapsextensions.MarkerOptions
import com.example.there.aircraftradar.R
import com.example.there.aircraftradar.data.model.FlightDetails
import com.example.there.aircraftradar.data.model.Flight
import com.example.there.aircraftradar.flightdetails.fragment.FlightDetailsFragment
import com.example.there.aircraftradar.view.ScrollViewMapFragment
import com.example.there.aircraftradar.util.extension.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLngBounds


class FlightDetailsMapFragment : FlightDetailsFragment() {

    override var flightDetails: FlightDetails? = null
        set(value) {
            if (value == null || field != null) return
            field = value
            displayRoute()
        }

    private var routeDisplayed = false

    private var map: GoogleMap? = null
    private var flightMarker: Marker? = null
    private var routeBounds: LatLngBounds? = null

    var onMapTouch: (() -> Unit)? = null
        set(value) {
            if (field != null) return
            field = value
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        routeDisplayed = savedInstanceState?.getBoolean(KEY_ROUTE_DISPLAYED) ?: false
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_flight_details_map, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        initMap()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putBoolean(KEY_ROUTE_DISPLAYED, routeDisplayed)
    }

    override fun onPause() {
        super.onPause()
        routeDisplayed = false
    }

    private fun initMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.flight_details_map_fragment) as ScrollViewMapFragment
        mapFragment.onTouch = onMapTouch
        mapFragment.getExtendedMapAsync {
            map = it
            map?.loadMapStyle(this@FlightDetailsMapFragment.context, R.raw.map_style)
            map?.initUiSettings()
            map?.setOnMapClickListener { routeBounds?.let { moveCameraToRouteBounds() } }
            displayRoute()
        }
    }

    private fun displayRoute() {
        if (!routeDisplayed) {
            routeDisplayed = true
            if (flightDetails != null && map != null) {
                val origin = flightDetails!!.airport.origin
                val destination = flightDetails!!.airport.destination

                flightMarker = map!!.addFlight(flight)
                if (origin == null || destination == null) {
                    moveCameraToFlight()
                    Toast.makeText(context, R.string.no_airport_info, Toast.LENGTH_LONG).show()
                    return
                }

                map!!.addMarker(MarkerOptions().position(origin.latLng).title(origin.name))
                map!!.addMarker(MarkerOptions().position(destination.latLng).title(destination.name))

                routeBounds = LatLngBounds.builder().makeBounds(origin.latLng, flight.position, destination.latLng)

                moveCameraToRouteBounds()
            }
        }
    }

    private fun moveCameraToRouteBounds() {
        map?.moveCamera(CameraUpdateFactory.newLatLngBounds(routeBounds, 300))
    }

    private fun moveCameraToFlight() {
        map?.moveCamera(CameraUpdateFactory.newLatLng(flight.position))
    }

    companion object {
        private const val KEY_ROUTE_DISPLAYED = "KEY_ROUTE_DISPLAYED"

        fun newInstance(flight: Flight, flightDetails: FlightDetails?): FlightDetailsMapFragment =
                FlightDetailsMapFragment().apply { putArguments(flight, flightDetails) }
    }
}
