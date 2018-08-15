package com.controllers

import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = StubApp::class)
class LifecycleTest {

  fun createController() = Robolectric.buildActivity(StubActivity::class.java)

  @Test
  fun testAttachedToStackAfterRecreation() {

    with(createController()) {
      setup()
      get().show(TestController())
      val bundle = tearDown()

      with(createController()) {
        // create another activity
        setup(bundle)
        with(get().top as TestController) {
          assertAttachedToStack()
        }
      }
    }
  }
}