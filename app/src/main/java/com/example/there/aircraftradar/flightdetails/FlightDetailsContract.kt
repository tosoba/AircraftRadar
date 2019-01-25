package com.example.there.aircraftradar.flightdetails

import com.example.there.aircraftradar.data.model.FlightDetails
import io.reactivex.Observable
import io.reactivex.Single

interface FlightDetailsContract {

    interface Interactor {
        fun loadFlightDetails(flightId: String): Single<FlightDetails>
    }

    interface ViewModel {
        fun loadFlightDetails(flightId: String)
    }
}