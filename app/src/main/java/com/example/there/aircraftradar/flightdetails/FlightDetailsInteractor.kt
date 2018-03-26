package com.example.there.aircraftradar.flightdetails

import com.example.there.aircraftradar.data.base.repository.BaseFlightDetailsRepository
import com.example.there.aircraftradar.data.impl.flightdetails.FlightDetails
import io.reactivex.Observable

class FlightDetailsInteractor(private val repository: BaseFlightDetailsRepository): FlightDetailsContract.Interactor {
    override fun loadFlightDetails(flightId: String): Observable<FlightDetails> = repository.loadFlightDetails(flightId)
}