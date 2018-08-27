package com.example.there.aircraftradar.flightdetails

import com.example.there.aircraftradar.data.model.FlightDetails
import io.reactivex.Observable

interface FlightDetailsContract {

    interface Interactor {
        fun loadFlightDetails(flightId: String): Observable<FlightDetails>
    }

    interface ViewModel {
        fun loadFlightDetails(flightId: String)
    }
}