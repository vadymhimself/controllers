package com.controllers

import android.databinding.DataBindingComponent
import android.databinding.MergedDataBinderMapper
import android.databinding.ViewDataBinding
import android.view.View
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLooper

@RunWith(RobolectricTestRunner::class)
@Config(application = StubApp::class)
class LifecycleConsumerTest {

  lateinit var activityController: ActivityController<StubActivity>

  @Before
  fun setUp() {
    activityController = Robolectric.buildActivity(StubActivity::class.java)
  }

  @Test
  fun testEventsReplayedToConsumer() {
    val activity = activityController.get()
    val consumer = TestLifecycleConsumerController()

    activityController.setup()
    activity.show(consumer)

    ShadowLooper.runUiThreadTasks()

    assert(consumer.onCreateCalled)
    assert(consumer.onStartCalled)
    assert(consumer.onResumeCalled)
    assert(!consumer.onPauseCalled)
  }

  @Test
  fun testOnPauseDeliveredToConsumer() {
    val activity = activityController.get()
    val consumer = TestLifecycleConsumerController()

    activityController.setup()
    activity.show(consumer)

    ShadowLooper.runUiThreadTasks()

    activityController.pause()

    ShadowLooper.runUiThreadTasks()

    assert(consumer.onPauseCalled)
  }

  @Test
  fun testOnStopAndOnPauseNotReplayedWhenRepeated() {
    val activity = activityController.get()
    val consumer = TestLifecycleConsumerController()

    activityController.setup()

    ShadowLooper.runUiThreadTasks()

    activityController.pause().stop().start().resume().visible()

    activity.show(consumer)

    ShadowLooper.runUiThreadTasks()

    assert(!consumer.onPauseCalled)
    assert(!consumer.onStopCalled)
  }

}

internal class LifecycleDataBinderMapper : MergedDataBinderMapper() {
  override fun getDataBinder(
    bindingComponent: DataBindingComponent?,
    view: View?,
    layoutId: Int
  ): ViewDataBinding? {
    return when (layoutId) {
      TestLifecycleConsumerController.FAKE_LAYOUT_ID -> LifecycleViewDataBinding(bindingComponent, view, 1)
      else -> null
    }
  }
}

internal class LifecycleViewDataBinding(
  component: DataBindingComponent?,
  view: View?,
  localFieldCount: Int
) : ViewDataBinding(component, view, localFieldCount) {

  var hasPendingBindings = false
  var controller : TestLifecycleConsumerController? = null

  init {
    setRootTag(view)
    invalidateAll()
  }

  override fun executeBindings() {
    // fake execution
    synchronized(this) {
      if (controller != null) {
        controller?.view?.subscribe(controller)
        hasPendingBindings = false
      }
    }
  }

  override fun onFieldChange(
    localFieldId: Int,
    `object`: Any?,
    fieldId: Int
  ): Boolean {
    return false
  }

  override fun invalidateAll() {
    hasPendingBindings = true
  }

  override fun hasPendingBindings() = hasPendingBindings

  override fun setVariable(
    variableId: Int,
    value: Any?
  ): Boolean {
    if (variableId == BR.controller) {
      synchronized(this) {
        if (controller != value) {
          hasPendingBindings = true
        }
      }

      updateRegistration(0, value as? TestLifecycleConsumerController)
      controller = value as? TestLifecycleConsumerController
      notifyPropertyChanged(variableId)
      super.requestRebind()

      return true
    }
    return false
  }

}