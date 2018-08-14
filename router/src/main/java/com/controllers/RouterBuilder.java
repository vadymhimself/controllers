package com.controllers;

import com.controllers.core.Router;

public class RouterBuilder<T extends Controller> {

  ContainerView view;

  public RouterBuilder<T> with(ContainerView view) {
    this.view = view;
    return this;
  }

  @SuppressWarnings("unchecked")
  public Router<T> build() {
    checkState();
    return new SimpleRouter<>(view.getStack());
  }

  private void checkState() {
    if (view == null) {
      throw new IllegalStateException();
    }
  }
}
