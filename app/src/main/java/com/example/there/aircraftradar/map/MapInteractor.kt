package com.example.there.aircraftradar.map

import com.example.there.aircraftradar.data.model.Flight
import com.example.there.aircraftradar.domain.BaseFlightsRepository
import com.google.android.gms.maps.model.LatLngBounds
import io.reactivex.Observable
import javax.inject.Inject

class MapInteractor @Inject constructor(
        private val flightsRepository: BaseFlightsRepository
) : MapContract.Interactor {

    override fun loadFlightsInBounds(bounds: LatLngBounds?): Observable<List<Flight>> = flightsRepository.loadFlights(bounds)
}