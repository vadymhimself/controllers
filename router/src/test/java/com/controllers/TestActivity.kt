package com.controllers

import android.os.Bundle

class TestActivity : ControllerActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.fake_layout)
    setControllerContainer(R.id.container)
  }
}
