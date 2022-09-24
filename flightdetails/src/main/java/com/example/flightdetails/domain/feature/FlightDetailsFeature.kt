package com.example.flightdetails.domain.feature

import com.example.core.data.Failure
import com.example.core.data.Success
import com.example.coreandroid.arch.CoroutineMiddleware
import com.example.coreandroid.arch.FeatureAction
import com.example.coreandroid.arch.event.ErrorEvent
import com.example.coreandroid.arch.feature.BaseFeature
import com.example.coreandroid.arch.feature.CoroutineFeature
import com.example.coreandroid.coroutine.CoroutineContextProvider
import com.example.coreandroid.model.FlightDetails
import com.example.flightdetails.domain.repo.IFlightDetailsRepository
import javax.inject.Inject


class FlightDetailsFeature @Inject constructor(
    contextProvider: CoroutineContextProvider,
    private val flightDetailsRepository: IFlightDetailsRepository
) : CoroutineFeature<FlightDetailsFeature.State, FlightDetailsFeature.Action, ErrorEvent>(
    contextProvider = contextProvider,
    initialState = State.INITIAL,
    reducer = { action, previousState ->
        if (action is Action.SetFlightDetails) previousState.copy(
            loading = false,
            flightDetails = action.flightDetails
        )
        else previousState
    },
    eventFactory = { action, _ ->
        if (action is Action.PostErrorEvent) ErrorEvent(action.throwable)
        null
    }
) {
    override val middleware: CoroutineMiddleware<Action, State>? = { action, _, dispatchPrivate ->
        when (action) {
            is Action.Load -> {
                val flightDetailsResult = loadDeferred {
                    flightDetailsRepository.loadFlightDetails(action.flightId)
                }
                when (flightDetailsResult) {
                    is Success -> Action.SetFlightDetails(flightDetailsResult.data)
                    is Failure -> {
                        dispatchPrivate(
                            Action.PostErrorEvent(
                                flightDetailsResult.error
                                    ?: Exception(BaseFeature.UNKNOWN_ERROR_MSG)
                            )
                        )
                        null
                    }
                }
            }
            else -> action
        }
    }

    data class State(val loading: Boolean, val flightDetails: FlightDetails?) {
        companion object {
            val INITIAL = State(false, null)
        }
    }

    sealed class Action : FeatureAction {
        data class Load(val flightId: String) : Action() {
            override val isPublic: Boolean = true
        }

        data class PostErrorEvent(val throwable: Throwable) : Action() {
            override val isPublic: Boolean = false
        }

        data class SetFlightDetails(val flightDetails: FlightDetails) : Action() {
            override val isPublic: Boolean = false
        }
    }
}