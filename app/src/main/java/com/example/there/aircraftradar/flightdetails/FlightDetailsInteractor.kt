package com.example.there.aircraftradar.flightdetails

import com.example.there.aircraftradar.data.model.FlightDetails
import com.example.there.aircraftradar.domain.BaseFlightsRepository
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class FlightDetailsInteractor @Inject constructor(
        private val repository: BaseFlightsRepository
) : FlightDetailsContract.Interactor {

    override fun loadFlightDetails(flightId: String): Single<FlightDetails> = repository.loadFlightDetails(flightId)
}