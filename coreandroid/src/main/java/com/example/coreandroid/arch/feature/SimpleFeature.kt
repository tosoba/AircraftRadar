package com.example.coreandroid.arch.feature

import com.example.coreandroid.arch.EventFactory
import com.example.coreandroid.arch.FeatureAction
import com.example.coreandroid.arch.Middleware
import com.example.coreandroid.arch.Reducer
import org.greenrobot.eventbus.EventBus


abstract class SimpleFeature<State : Any, Action : FeatureAction, Event : Any>(
        initialState: State,
        reducer: Reducer<Action, State>,
        eventFactory: EventFactory<Action, State, Event>? = null
) : BaseFeature<State, Action, Event>(initialState, reducer, eventFactory) {

    open val middleware: Middleware<Action, State>? = null

    override fun dispatchPrivate(action: Action) {
        val actionToDispatch = middleware?.invoke(action, currentState, ::dispatchPrivate) ?: action
        mutableState.value = reducer(actionToDispatch, currentState)
        eventFactory?.invoke(actionToDispatch, currentState)?.let {
            EventBus.getDefault().post(it)
        }
    }
}

