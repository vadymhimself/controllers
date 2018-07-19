package com.controllers

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = TestApp::class)
class TransactionsTest {

  lateinit var activity : TestActivity

  @Before
  fun setUp() {
    activity = Robolectric.buildActivity(TestActivity::class.java)
        .create()
        .resume()
        .visible()
        .get()
  }

  @Test
  fun test() {
    // FAILS! robolectric bug https://github.com/robolectric/robolectric/issues/3789
    activity.replace(TestController())
    assertThat(activity.stack).isNotEmpty()
  }
}