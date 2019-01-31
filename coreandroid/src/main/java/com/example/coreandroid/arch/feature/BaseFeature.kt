package com.example.coreandroid.arch.feature

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.example.coreandroid.arch.EventFactory
import com.example.coreandroid.arch.FeatureAction
import com.example.coreandroid.arch.Reducer


abstract class BaseFeature<State : Any, Action : FeatureAction, Event : Any>(
        initialState: State,
        protected val reducer: Reducer<Action, State>,
        protected val eventFactory: EventFactory<Action, State, Event>? = null
) {
    protected val mutableState: MutableLiveData<State> = MutableLiveData<State>().apply {
        value = initialState
    }

    val liveState: LiveData<State>
        get() = mutableState

    val currentState: State
        get() = liveState.value!!

    fun dispatch(action: Action) {
        if (!action.isPublic) throw IllegalArgumentException(PRIVATE_ACTION_ERROR_MSG)
        dispatchPrivate(action)
    }

    protected abstract fun dispatchPrivate(action: Action)

    companion object {
        private const val PRIVATE_ACTION_ERROR_MSG = "Private action cannot be dispatched using feature's public dispatch method."
        const val UNKNOWN_ERROR_MSG = "Unknown error has occurred."
    }
}