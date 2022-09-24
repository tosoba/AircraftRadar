package com.example.coreandroid.arch.feature

import com.example.coreandroid.arch.CoroutineMiddleware
import com.example.coreandroid.arch.EventFactory
import com.example.coreandroid.arch.FeatureAction
import com.example.coreandroid.arch.Reducer
import com.example.coreandroid.coroutine.CoroutineContextProvider
import kotlinx.coroutines.*
import org.greenrobot.eventbus.EventBus
import kotlin.coroutines.CoroutineContext

abstract class CoroutineFeature<State : Any, Action : FeatureAction, Event : Any>(
    private val contextProvider: CoroutineContextProvider,
    initialState: State,
    reducer: Reducer<Action, State>,
    eventFactory: EventFactory<Action, State, Event>? = null
) : BaseFeature<State, Action, Event>(initialState, reducer, eventFactory), CoroutineScope {

    private val jobs = mutableListOf<Job>()

    protected val main: CoroutineContext by lazy { contextProvider.main }
    protected val background: CoroutineContext by lazy { contextProvider.io }

    override val coroutineContext: CoroutineContext by lazy { main }

    open val middleware: CoroutineMiddleware<Action, State>? = null

    override fun dispatchPrivate(action: Action) {
        execute {
            val actionToDispatch = loadDeferred {
                middleware?.invoke(action, currentState, ::dispatchPrivate) ?: action
            }
            mutableState.value = reducer(actionToDispatch, currentState)
            eventFactory?.invoke(actionToDispatch, currentState)?.let {
                EventBus.getDefault().post(it)
            }
        }
    }

    fun dispose() {
        jobs.forEach(::cancelJob)
        jobs.clear()
    }

    protected suspend fun <T : Any> defer(
        dataProvider: suspend () -> T
    ): Deferred<T> = async(background) { dataProvider() }

    protected suspend fun <T : Any> loadDeferred(
        dataProvider: suspend () -> T
    ): T = async(background) { dataProvider() }.run {
        jobs.add(this)
        await()
    }

    protected fun execute(action: suspend () -> Unit) {
        jobs.add(launch(main) { action() })
    }

    private fun cancelJob(job: Job) {
        if (job.isActive) job.cancel()
    }
}