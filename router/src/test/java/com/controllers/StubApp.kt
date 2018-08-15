package com.controllers

import android.app.Application

class StubApp: Application() {

  override fun onCreate() {
    super.onCreate()
    setTheme(R.style.Theme_AppCompat) //or just R.style.Theme_AppCompat
  }

}