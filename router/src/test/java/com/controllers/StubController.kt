package com.controllers

import android.databinding.ViewDataBinding

class StubController : Controller<ViewDataBinding>() {

  override fun getLayoutId(): Int = R.layout.fake_layout

}