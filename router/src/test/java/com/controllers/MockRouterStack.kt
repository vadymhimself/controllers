package com.controllers

internal class MockRouterStack<T : IController> : RouterStack<T>(StubRouter()) {
  fun populate(vararg items: T) {
    transaction { t ->
      items.forEach {
        t.add(it)
      }
      t.commit()
    }
  }
}