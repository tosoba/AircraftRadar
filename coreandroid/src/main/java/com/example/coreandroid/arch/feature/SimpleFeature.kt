package com.example.coreandroid.arch.feature

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.example.coreandroid.arch.EventFactory
import com.example.coreandroid.arch.Middleware
import com.example.coreandroid.arch.Reducer
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
        mutableState.value = reducer(actionToDispatch, currentState)
        eventFactory?.let {
            EventBus.getDefault().post(it(action, currentState))
        }
    }
}

