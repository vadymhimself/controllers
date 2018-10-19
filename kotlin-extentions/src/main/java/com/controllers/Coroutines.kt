package com.controllers

import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.Main
import kotlinx.coroutines.experimental.cancel
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext
import kotlin.coroutines.experimental.CoroutineContext

/**
 * TODO: postpone coroutine launch until controller is attached
 * TODO: make a child of activity's job
 */
fun <T : Controller<*>> T.async(block: suspend AsyncHandle.() -> Unit): Job {

  // launch a new UI coroutine in the context of new job
  return GlobalScope.launch(Dispatchers.Main) {
    val jobObserver = JobObserver(coroutineContext)
    this@async.addObserver(jobObserver)
    try {
      block(AsyncHandle())
    } finally {
      this@async.removeObserver(jobObserver)
    }
  }
}

class AsyncHandle {
  suspend inline fun <R> await(crossinline block: suspend () -> R): R {
    return withContext(Dispatchers.Default) {
      block()
    }
  }
}

class JobObserver(private val context: CoroutineContext) : Controller.Observer {
  override fun onAttachedToRouter(observable: Controller<*>) {

  }

  override fun onDetachedFromRouter(observable: Controller<*>) {
    // TODO may be null if context is deserialized
    context.cancel()
    observable.removeObserver(this)
  }

  override fun onAttachedToScreen(observable: Controller<*>) {

  }

  override fun onDetachedFromScreen(observable: Controller<*>) {

  }

  override fun onRestored(observable: Controller<*>) {

  }

}