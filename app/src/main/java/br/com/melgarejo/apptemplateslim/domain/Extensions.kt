package br.com.melgarejo.apptemplateslim.domain

import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.async

suspend fun <T> async(block: suspend CoroutineScope.() -> T): Deferred<T> {
    return GlobalScope.async { block() }
}

suspend fun <T> asyncAwait(block: suspend CoroutineScope.() -> T): T {
    return async(block).await()
}

fun launchAsync(block: suspend CoroutineScope.() -> Unit): Job {
    return GlobalScope.async { block() }
}