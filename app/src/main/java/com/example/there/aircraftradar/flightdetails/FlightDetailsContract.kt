package com.example.there.aircraftradar.flightdetails

import com.example.there.aircraftradar.data.impl.flightdetails.FlightDetailsResponse
import io.reactivex.Observable

interface FlightDetailsContract {
    interface Interactor {
        fun loadFlightDetails(flightId: String): Observable<FlightDetailsResponse>
    }

    interface ViewModel {
        fun loadFlightDetails(flightId: String)
    }
}