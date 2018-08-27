package com.example.there.aircraftradar.data

import com.example.there.aircraftradar.data.model.Flight
import com.example.there.aircraftradar.data.model.FlightDetails
import com.example.there.aircraftradar.domain.BaseFlightsRepository
import com.google.android.gms.maps.model.LatLngBounds
import io.reactivex.Observable
import javax.inject.Inject

class FlightsRepository @Inject constructor(
        private val service: FlightRadarApiService
) : BaseFlightsRepository {

    override fun loadFlights(bounds: LatLngBounds?): Observable<List<Flight>> {
        val boundsStr = if (bounds != null) {
            "${bounds.northeast.latitude},${bounds.southwest.latitude},${bounds.southwest.longitude},${bounds.northeast.longitude}"
        } else null
        return service.loadFlights(bounds = boundsStr).map(FlightsMapper::fromEntity)
    }

    override fun loadFlightDetails(flightId: String): Observable<FlightDetails> = service.loadFlightDetails(flightId)
}