package com.controllers

import android.databinding.ViewDataBinding
import android.os.Bundle
import com.controllers.AbstractController.ViewLifecycleConsumer

/**
 * Controller that consumes it's own view events
 */
internal open class TestLifecycleConsumerController : Controller<ViewDataBinding>(),
    ViewLifecycleConsumer {

  var onCreateCalled: Boolean = false
  var onStartCalled: Boolean = false
  var onResumeCalled: Boolean = false
  var onPauseCalled: Boolean = false
  var onStopCalled: Boolean = false
  var onDestroyCalled: Boolean = false

  companion object Const {
    val FAKE_LAYOUT_ID = R.layout.layout_fake_2
  }

  override fun onCreate(var1: Bundle?) {
    onCreateCalled = true
  }

  override fun onStart() {
    onStartCalled = true
  }

  override fun onResume() {
    onResumeCalled = true
  }

  override fun onPause() {
    onPauseCalled = true
  }

  override fun onStop() {
   onStopCalled = true
  }

  override fun onDestroy() {
    onDestroyCalled = true
  }

  override fun getLayoutId() = FAKE_LAYOUT_ID
}