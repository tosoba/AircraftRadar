package com.example.coreandroid.coroutine

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.Unconfined
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class CoroutineContextProviderImpl @Inject constructor() : CoroutineContextProvider {
    override val io: CoroutineContext by lazy { Default }
    override val main: CoroutineContext by lazy { Dispatchers.Main }
}

class TestCoroutineContextProviderImpl @Inject constructor() : CoroutineContextProvider {

    @ExperimentalCoroutinesApi
    override val io: CoroutineContext by lazy { Unconfined }

    @ExperimentalCoroutinesApi
    override val main: CoroutineContext by lazy { Unconfined }
}

interface CoroutineContextProvider {
    val main: CoroutineContext
    val io: CoroutineContext
}