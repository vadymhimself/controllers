package com.controllers

import android.os.Handler
import android.os.Looper
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.cancel
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext
import kotlin.coroutines.experimental.CoroutineContext
import kotlinx.coroutines.experimental.CommonPool as BG_CONTEXT
import kotlinx.coroutines.experimental.android.UI as UI_CONTEXT

/**
 * TODO: postpone coroutine launch until controller is attached
 * TODO: make a child of activity's job
 */
fun <T : Controller<*>> T.async(block: suspend AsyncHandle.() -> Unit): Job {

  // launch a new UI coroutine in the context of new job
  return launch(UI_CONTEXT) {
    val jobObserver = JobObserver(coroutineContext)
    this@async.addObserver(jobObserver)
    try {
      block(AsyncHandle())
    } catch (e: Throwable) {
      if (!isActive) {
        // if an exception is thrown in a cancelled coroutine, it will be ignored by
        // the bad coroutines design https://github.com/Kotlin/kotlinx.coroutines/issues/333
        // so we post it in a main thread to actually kill the app
        Handler(Looper.getMainLooper()).post({
          // the problem is that it can never be caught
          throw Exception("Exception thrown in a cancelled coroutine $coroutineContext", e)
        })
      }
      throw e
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