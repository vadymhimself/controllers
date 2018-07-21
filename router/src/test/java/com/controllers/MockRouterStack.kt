package com.controllers

internal class MockRouterStack<T : IController> : RouterStack<T>(StubRouter()) {
  fun populate(vararg items: T) {
    beginTransaction { trasaction ->
      items.forEach {
        trasaction.add(it)
      }
      trasaction.commit()
    }
  }
}