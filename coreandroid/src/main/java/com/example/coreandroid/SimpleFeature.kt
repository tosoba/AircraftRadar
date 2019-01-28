package com.example.coreandroid

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import org.greenrobot.eventbus.EventBus

class SimpleFeature<State : Any, Action : Any, Event : Any>(
        initialState: State,
        private val reduce: Action.(State) -> State,
        private val middleware: ((Action, State) -> Unit)? = null,
        private val eventFactory: ((Action, State) -> Event)? = null
) {
    private val mutableState: MutableLiveData<State> = MutableLiveData<State>().apply { value = initialState }

    val state: LiveData<State>
        get() = mutableState

    fun dispatch(action: Action) {
        middleware?.invoke(action, state.value!!)
        mutableState.value = action.reduce(mutableState.value!!)
        eventFactory?.let {
            EventBus.getDefault().post(it(action, state.value!!))
        }
    }
}