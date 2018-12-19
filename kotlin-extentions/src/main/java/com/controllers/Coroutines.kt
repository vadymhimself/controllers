package com.controllers

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * TODO: postpone coroutine launch until controller is attached
 * TODO: make a child of activity's job
 */
fun <T : Controller<*>> T.async(block: suspend AsyncHandle.() -> Unit): Job {

  // launch a new UI coroutine in the context of new job
  val job = Job()
  val coroutineScoupe = object: CoroutineScope {
    override val coroutineContext = job + Dispatchers.Main
  }
 return coroutineScoupe.launch {
    val jobObserver = JobObserver(job)
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

class JobObserver(private val job: Job) : Controller.Observer {
  override fun onAttachedToRouter(observable: Controller<*>) {

  }

  override fun onDetachedFromRouter(observable: Controller<*>) {
    // TODO may be null if context is deserialized
    job.cancel()
    observable.removeObserver(this)
  }

  override fun onAttachedToScreen(observable: Controller<*>) {

  }

  override fun onDetachedFromScreen(observable: Controller<*>) {

  }

  override fun onRestored(observable: Controller<*>) {

  }

}