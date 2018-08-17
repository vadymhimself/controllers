package com.controllers;

import com.controllers.core.Router;
import com.controllers.core.RouterStack;

public class RouterBuilder<T extends Controller> {

  @SuppressWarnings("unchecked")
  public Router<T> build() {
    return new SimpleRouter<>(new RouterStack<T>());
  }
}
