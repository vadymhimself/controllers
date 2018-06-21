package com.controllers

import android.util.Log
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.cancel
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext
import java.net.UnknownHostException
import kotlin.coroutines.experimental.CoroutineContext

import kotlinx.coroutines.experimental.CommonPool as BG_CONTEXT
import kotlinx.coroutines.experimental.android.UI as UI_CONTEXT

/**
 * TODO: postpone coroutine launch until controller is attached
 * TODO: make a child of activity's job
 */
fun <T : Controller<*>> T.async(block: suspend AsyncHandle.() -> Unit): Job {

  // take a snapshot of the stack trace in order to make the exception inside
  // the coroutine more readable
  val snapshot = Exception()

  // launch a new UI coroutine in the context of new job
  return launch(UI_CONTEXT) {
    val jobObserver = JobObserver(coroutineContext)
    this@async.addObserver(jobObserver)
    try {
      block(AsyncHandle())
    } catch (e: Exception) {
      val ex = Exception("Runtime exception in $coroutineContext", e)
      ex.stackTrace = snapshot.stackTrace
      // TODO check cause recursively
      if (e is UnknownHostException || e.cause is UnknownHostException) {
        // Log does not print stack of the UnknownHostException
        ex.printStackTrace()
      } else {
        Log.e(coroutineContext.toString(), "Runtime exception", ex)
      }
      throw ex
    } finally {
      this@async.removeObserver(jobObserver)
    }
  }
}

class AsyncHandle {
  suspend inline fun <R> await(crossinline block: suspend () -> R): R {
    return withContext(BG_CONTEXT) {
      block()
    }
  }
}

class JobObserver(private val context: CoroutineContext) : ObservableController.Observer {
  override fun onAttachedToStack(observable: ObservableController<*>) {

  }

  override fun onDetachedFromStack(observable: ObservableController<*>) {
    // TODO may be null if context is deserialized
    context.cancel()
    observable.removeObserver(this)
  }

  override fun onAttachedToScreen(observable: ObservableController<*>) {

  }

  override fun onDetachedFromScreen(observable: ObservableController<*>) {

  }

  override fun onRestored(observable: ObservableController<*>) {

  }

}