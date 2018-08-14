package com.controllers

import com.controllers.core.Router
import com.controllers.core.Router.Transition

class StubRouter : Router<Controller<*>> {
  override fun make(transition: Transition<Controller<*>>?): Boolean {
    return false
  }

  override fun <T : Any?> findByClass(clazz: Class<T>?): T? {
    return null
  }

  override fun findByTag(tag: Any?): Controller<*>? {
    return null
  }

  override fun getPrevious(): Controller<*>? {
    return null
  }

  override fun getTop(): Controller<*>? {
    return null
  }

  override fun getBottom(): Controller<*>? {
    return null
  }
}