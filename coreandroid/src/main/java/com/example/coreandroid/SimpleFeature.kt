package com.example.coreandroid

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import org.greenrobot.eventbus.EventBus

class SimpleFeature<State : Any, Action : Any, Event : Any>(
        initialState: State,
        private val reduce: Action.(State) -> State,
        private val middleware: ((Action, State, (Action) -> Unit) -> Action)? = null,
        private val eventFactory: ((Action, State) -> Event)? = null
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
        mutableState.value = actionToDispatch.reduce(currentState)
        eventFactory?.let {
            EventBus.getDefault().post(it(action, currentState))
        }
    }
}