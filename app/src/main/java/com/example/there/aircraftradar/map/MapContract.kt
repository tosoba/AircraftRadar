package com.example.there.aircraftradar.map

import com.example.there.aircraftradar.data.impl.flights.Flight
import com.google.android.gms.maps.model.LatLngBounds
import io.reactivex.Observable

interface MapContract {
    interface Interactor {
        fun loadFlightsInBounds(bounds: LatLngBounds?): Observable<List<Flight>>
    }

    interface ViewModel {
        fun loadFlightsInBounds(bounds: LatLngBounds?)
    }
}