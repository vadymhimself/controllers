package com.controllers

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class StubActivity : AppCompatActivity() {

  val router = RouterBuilder<Controller<*>>().build()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.layout_fake)
  }

  fun show(c : Controller<*>) {
    router.make(FragmentTransitions.Show(R.id.container, this, supportFragmentManager, 0, 0, c))
  }
}
