package com.controllers

import android.util.Log
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext

import kotlinx.coroutines.experimental.CommonPool as BG_CONTEXT
import kotlinx.coroutines.experimental.android.UI as UI_CONTEXT

/**
 * TODO: postpone coroutine launch until controller is attached
 */
fun <T : Controller<*>> T.async(block: suspend AsyncHandle.() -> Unit): Job {

    val job = Job()
    // subscribe Job object to controller's lifecycle
    this.observeWith(JobObserver(job))

    val exception = Exception()

    // launch a new UI coroutine in the context of new job
    launch(job + UI_CONTEXT) {
        val asyncHandle = AsyncHandle()
        try {
            block(asyncHandle)
        } catch (e: Exception) {
            e.stackTrace = exception.stackTrace
            throw e
        }
    }

    return job
}

@PublishedApi
internal fun <T : ObservableController<*>> T.observeWith(obs : ObservableController.Observer) {
    this.addObserver(obs)
}

class AsyncHandle {
    suspend inline fun <R> await(crossinline block: suspend () -> R) : R {
        return withContext(BG_CONTEXT) {
            block()
        }
    }
}

class JobObserver(private val job : Job) : ObservableController.Observer {
    override fun onAttachedToStack(observable: ObservableController<*>) {

    }

    override fun onDetachedFromStack(observable: ObservableController<*>) {
        job.cancel()
        observable.removeObserver(this)
    }

    override fun onAttachedToScreen(observable: ObservableController<*>) {

    }

    override fun onDetachedFromScreen(observable: ObservableController<*>) {

    }

    override fun onRestored(observable: ObservableController<*>) {

    }

}