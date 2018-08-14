package com.controllers

import android.app.Activity
import android.os.Bundle
import org.robolectric.android.controller.ActivityController

fun <T: Activity> ActivityController<T>.tearDown(): Bundle {
  val bundle = Bundle()
  pause()
  saveInstanceState(bundle)
  stop()
  destroy() // destroy makes the bundle serialize?
  return bundle
}
