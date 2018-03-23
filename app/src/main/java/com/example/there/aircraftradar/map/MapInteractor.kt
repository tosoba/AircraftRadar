package com.example.there.aircraftradar.map

import com.example.there.aircraftradar.data.base.repository.BaseFlightsRepository
import com.example.there.aircraftradar.data.impl.flights.Flight
import com.google.android.gms.maps.model.LatLngBounds
import io.reactivex.Observable

class MapInteractor(private val flightsRepository: BaseFlightsRepository): MapContract.Interactor {
    override fun loadFlightsInBounds(bounds: LatLngBounds?): Observable<List<Flight>> = flightsRepository.loadFlights(bounds)
}