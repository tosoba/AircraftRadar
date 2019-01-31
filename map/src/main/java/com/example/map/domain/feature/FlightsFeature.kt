package com.example.map.domain.feature

import com.example.core.data.Failure
import com.example.core.data.Success
import com.example.coreandroid.arch.CoroutineMiddleware
import com.example.coreandroid.arch.FeatureAction
import com.example.coreandroid.arch.event.ErrorEvent
import com.example.coreandroid.arch.feature.BaseFeature
import com.example.coreandroid.arch.feature.CoroutineFeature
import com.example.coreandroid.coroutine.CoroutineContextProvider
import com.example.coreandroid.model.Flight
import com.example.map.data.MapRepository
import javax.inject.Inject


class FlightsFeature @Inject constructor(
        contextProvider: CoroutineContextProvider,
        private val mapRepository: MapRepository
) : CoroutineFeature<FlightsFeature.State, FlightsFeature.Action, ErrorEvent>(
        contextProvider = contextProvider,
        initialState = FlightsFeature.State.INITIAL,
        reducer = { action, previousState ->
            if (action is Action.SetFlights) previousState.copy(
                    loading = false,
                    flights = action.newFlights
            )

            previousState
        },
        eventFactory = { action, _ ->
            if (action is Action.PostErrorEvent) ErrorEvent(action.throwable)
            null
        }
) {
    override val middleware: CoroutineMiddleware<Action, State>? = { action, _, dispatchPrivate ->
        when (action) {
            is Action.Load -> {
                val flightsResult = loadDeferred(mapRepository::loadFlights)
                when (flightsResult) {
                    is Success -> Action.SetFlights(flightsResult.data)
                    is Failure -> {
                        dispatchPrivate(Action.PostErrorEvent(flightsResult.error
                                ?: Exception(BaseFeature.UNKNOWN_ERROR_MSG)))
                        null
                    }
                }

            }
            else -> action
        }
    }

    data class State(val loading: Boolean, val flights: List<Flight>) {
        companion object {
            val INITIAL = State(false, emptyList())
        }
    }

    sealed class Action : FeatureAction {
        object Load : Action() {
            override val isPublic: Boolean = true
        }

        data class PostErrorEvent(val throwable: Throwable) : Action() {
            override val isPublic: Boolean = false
        }

        data class SetFlights(val newFlights: List<Flight>) : Action() {
            override val isPublic: Boolean = false
        }
    }
}