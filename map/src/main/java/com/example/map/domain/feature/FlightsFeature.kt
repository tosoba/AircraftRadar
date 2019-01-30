package com.example.map.domain.feature

import com.example.coreandroid.arch.FeatureAction
import com.example.coreandroid.arch.feature.CoroutineFeature
import com.example.coreandroid.coroutine.CoroutineContextProvider
import com.example.coreandroid.model.Flight


class FlightsFeature(
        contextProvider: CoroutineContextProvider
) : CoroutineFeature<FlightsFeature.State, FlightsFeature.Action, Unit>(
        contextProvider = contextProvider,
        initialState = FlightsFeature.State.INITIAL,
        reducer = { action, previousState ->

        },
        middleware = { action, currentState, dispatch ->

        }
) {

    data class State(val loading: Boolean, val flights: List<Flight>) {
        companion object {
            val INITIAL = State(false, emptyList())
        }
    }

    sealed class Action : FeatureAction {
        object Load : Action() {
            override val isPublic: Boolean = true
        }

        data class SetFlights(val newFlights: List<Flight>) : Action() {
            override val isPublic: Boolean = false
        }
    }
}