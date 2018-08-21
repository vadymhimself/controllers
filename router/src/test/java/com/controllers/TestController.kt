package com.controllers

import android.databinding.ViewDataBinding

class TestController : Controller<ViewDataBinding>() {

  fun assertAttachedToStack() {
    assert(isAttachedToRouter)
  }

  fun assertAttachedToScreen() {
    assert(isAttachedToScreen)
  }

  override fun getLayoutId(): Int = R.layout.layout_fake

}