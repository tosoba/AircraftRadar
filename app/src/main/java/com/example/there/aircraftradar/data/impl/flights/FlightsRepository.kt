package com.example.there.aircraftradar.data.impl.flights

import com.example.there.aircraftradar.data.FlightRadarApiService
import com.example.there.aircraftradar.data.base.repository.BaseFlightsRepository
import com.google.android.gms.maps.model.LatLngBounds
import io.reactivex.Observable

class FlightsRepository(private val service: FlightRadarApiService,
                        private val mapper: FlightsMapper): BaseFlightsRepository {
    override fun loadFlights(bounds: LatLngBounds): Observable<List<Flight>> {
        val boundsStr = "${bounds.northeast.latitude},${bounds.southwest.latitude},${bounds.southwest.longitude},${bounds.northeast.longitude}"
        return service.loadFlights(bounds = boundsStr).map(mapper::fromEntity)
    }
}