package com.controllers

import android.databinding.ViewDataBinding

class TestController : Controller<ViewDataBinding>() {

  override fun getLayoutId(): Int = R.layout.fake_layout

}