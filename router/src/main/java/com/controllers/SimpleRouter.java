package com.controllers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.controllers.core.Router;
import com.controllers.core.RouterStack;
import com.controllers.core.ViewModel;
import java.io.Serializable;
import java.util.Iterator;

class SimpleRouter<T extends ViewModel> implements Router<T>, Serializable {

  private final RouterStack<T> stack;

  SimpleRouter(@NonNull RouterStack<T> router) {
    this.stack = router;
  }

  @Override
  public boolean make(Transition<T> transition) {
    return transition.run(stack);
  }

  @Override
  @Nullable
  public <C> C findByClass(Class<C> clazz) {
    for (T t : stack) {
      if (clazz.isAssignableFrom(t.getClass())) {
        return clazz.cast(t);
      }
    }
    return null;
  }

  @Nullable
  @Override
  public T getTop() {
    return stack.peek();
  }

  @Nullable
  @Override
  public T getPrevious() {
    return stack.peek(1);
  }

  @Nullable
  @Override
  public T getBottom() {
    return stack.peek(stack.size() - 1);
  }

  @Override
  public int size() {
    return stack.size();
  }

  @NonNull
  @Override
  public Iterator<T> iterator() {
    return stack.iterator();
  }
}
