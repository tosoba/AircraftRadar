package com.example.coreandroid.arch

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import org.greenrobot.eventbus.EventBus

open class SimpleFeature<State : Any, Action : Any, Event : Any>(
        initialState: State,
        private val reducer: Reducer<Action, State>,
        private val middleware: Middleware<Action, State>? = null,
        private val eventFactory: EventFactory<Action, State, Event>? = null
) {
    private val mutableState: MutableLiveData<State> = MutableLiveData<State>().apply {
        value = initialState
    }

    val liveState: LiveData<State>
        get() = mutableState

    val currentState: State
        get() = liveState.value!!

    fun dispatch(action: Action) {
        val actionToDispatch = middleware?.invoke(action, currentState, ::dispatch) ?: action
        mutableState.value = actionToDispatch.reducer(currentState)
        eventFactory?.let {
            EventBus.getDefault().post(it(action, currentState))
        }
    }
}

// for running coroutines
// have like a list of jobs here like in new games plus
// if it's actually necessary - or maybe there's no need to do that since jobs can be managed by classes that inherit from SimpleFeature
class CoroutineFeature<State : Any, Action : Any, Event : Any>(
        initialState: State,
        reducer: Reducer<Action, State>,
        middleware: Middleware<Action, State>? = null,
        eventFactory: EventFactory<Action, State, Event>? = null
) : SimpleFeature<State, Action, Event>(initialState, reducer, middleware, eventFactory)