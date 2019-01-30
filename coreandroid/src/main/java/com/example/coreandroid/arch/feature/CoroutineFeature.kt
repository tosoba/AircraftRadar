package com.example.coreandroid.arch.feature

import com.example.coreandroid.arch.EventFactory
import com.example.coreandroid.arch.Middleware
import com.example.coreandroid.arch.Reducer
import com.example.coreandroid.coroutine.CoroutineContextProvider
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

open class CoroutineFeature<State : Any, Action : Any, Event : Any>(
        private val contextProvider: CoroutineContextProvider,
        initialState: State,
        reducer: Reducer<Action, State>,
        middleware: Middleware<Action, State>? = null,
        eventFactory: EventFactory<Action, State, Event>? = null
) : SimpleFeature<State, Action, Event>(initialState, reducer, middleware, eventFactory), CoroutineScope {

    private val jobs = mutableListOf<Job>()

    protected val main: CoroutineContext by lazy { contextProvider.main }
    protected val background: CoroutineContext by lazy { contextProvider.io }

    override val coroutineContext: CoroutineContext by lazy { main }

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

    fun dispose() {
        jobs.forEach(::cancelJob)
        jobs.clear()
    }

    private fun cancelJob(job: Job) {
        if (job.isActive) job.cancel()
    }
}