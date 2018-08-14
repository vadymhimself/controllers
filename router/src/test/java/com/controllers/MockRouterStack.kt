package com.controllers

import com.controllers.core.RouterStack
import com.controllers.core.ViewModel

internal class MockRouterStack<T : ViewModel<*>> : RouterStack<T>() {
  fun populate(vararg items: T) {
    transaction { t ->
      items.forEach {
        t.add(it)
      }
      t.commit()
    }
  }
}