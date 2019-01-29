package com.example.coreandroid.retrofit

import com.example.core.data.Failure
import com.example.core.data.Mappable
import com.example.core.data.Result
import com.example.core.data.Success
import com.example.coreandroid.BuildConfig
import com.google.gson.JsonParseException
import kotlinx.coroutines.delay
import retrofit2.Call
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private const val DEFAULT_RETRY_ATTEMPTS = 3
private const val REPEAT_DELAY = 5000L

suspend fun <T : Mappable<R>, R : Any> Call<T>.awaitResult(): Result<R> {
    val callWrapper: () -> Result<R>? = {
        val call = clone()

        try {
            val response = call.execute()

            val result = response?.body()?.run {
                if (isValid) Success(data)
                else Failure(mapError(HttpException(response)))
            }

            val errorResult = response?.errorBody()?.run { Failure(mapError(HttpException(response))) }

            result ?: errorResult
        } catch (error: Throwable) {
            if (BuildConfig.DEBUG) {
                error.printStackTrace()
            }

            Failure(mapError(error))
        }
    }

    val dataProvider: suspend () -> Result<R> = {
        suspendCoroutine { continuation ->
            val data = callWrapper()
            data?.run { continuation.resume(this) }
        }
    }

    val dataInvalidator: (Result<R>) -> Boolean = { data ->
        data is Failure && (data.error == NetworkException || data.error == ServerError)
    }

    repeat(DEFAULT_RETRY_ATTEMPTS - 1) {
        //first we try to run the data two times, if it's okay, we return it
        val data = dataProvider()
        if (!dataInvalidator(data)) return data
        delay(REPEAT_DELAY)
    }

    return dataProvider() //final attempt
}

private val serverErrorCodes = 500..600
private val authenticationErrorCodes = 400..499

private fun mapError(error: Throwable?): Throwable? = when {
    error is JsonParseException -> ApiDataTransformationException
    error is IOException -> NetworkException
    error is ConnectException -> NetworkException
    error is HttpException && error.code() in serverErrorCodes -> ServerError
    error is HttpException && error.code() in authenticationErrorCodes -> AuthenticationError
    else -> error
}