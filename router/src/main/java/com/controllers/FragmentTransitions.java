package com.controllers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.AnimRes;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import com.controllers.core.Router;
import com.controllers.core.RouterStack;

/**
 * TODO: unit test transitions
 */
public abstract class FragmentTransitions {

  static abstract class FragmentTransition implements Router.Transition<Controller> {

    final @IdRes int containerId;
    final Activity activity;
    final FragmentManager fragmentManager;

    @AnimRes int enter;
    @AnimRes int exit;

    public FragmentTransition(int containerId, Activity activity,
        FragmentManager fragmentManager, int enter, int exit) {
      this.containerId = containerId;
      this.activity = activity;
      this.fragmentManager = fragmentManager;
      this.enter = enter;
      this.exit = exit;
    }

    final boolean busy(RouterStack<Controller> stack) {
      if (stack.isInTransaction() || activity.isFinishing()) {
        // TODO: logger
        Log.w(Const.LOG_PREFIX, "ignored transition call for router in transaction");
        return true;
      }

      return false;
    }

    final void applyTransition(@IdRes int containerId, final Controller<?> next) {

      @SuppressLint("CommitTransaction")
      FragmentTransaction transaction = fragmentManager.beginTransaction();

      if (enter != 0 || exit != 0) {
        transaction = transaction.setCustomAnimations(enter, exit);
      }

      FragmentBindingView view = next.getView();
      if (view == null) {
        view = new FragmentBindingView<>(next);
      }

      transaction.replace(containerId, view, next.ID);
      transaction.commitNow(); // TODO: re-render top controller on exception (stupid fragments leave the view in a broken state)
    }
  }

  public static class Show extends FragmentTransition {

    final Controller next;

    public Show(int containerId, Activity activity,
        FragmentManager fragmentManager, int enter, int exit, Controller next) {
      super(containerId, activity, fragmentManager, enter, exit);
      this.next = next;
    }

    @Override
    public boolean run(RouterStack<Controller> stack) {
      if (busy(stack)) return false;

      stack.transaction(new RouterStack.TransactionBlock<Controller>() {
        @Override public void run(RouterStack.Transaction<Controller> transaction) {
          transaction.add(next);
          applyTransition(containerId, next);
        }
      });

      return true;
    }
  }

  public static class Back extends FragmentTransition {

    public Back(int containerId, Activity activity,
        FragmentManager fragmentManager, int enter, int exit) {
      super(containerId, activity, fragmentManager, enter, exit);
    }

    @Override
    public boolean run(RouterStack<Controller> stack) {
      if (stack.size() <= 1) throw new IllegalStateException("Stack must be bigger than 1");

      if (busy(stack)) return false;

      final Controller prev = stack.peek();
      final Controller next = stack.peek(1);

      if (prev != null && prev.beforeChanged(next)) {
        return false;
      }

      stack.transaction(new RouterStack.TransactionBlock<Controller>() {
        @Override
        public void run(RouterStack.Transaction<Controller> transaction) {
          transaction.pop();
          applyTransition(containerId, next);
        }
      });

      return true;
    }
  }

  public static class GoBackTo extends FragmentTransition {

    final Controller next;

    public GoBackTo(int containerId, Activity activity,
        FragmentManager fragmentManager, int enter, int exit, Controller next) {
      super(containerId, activity, fragmentManager, enter, exit);
      this.next = next;
    }

    @Override
    public boolean run(RouterStack<Controller> stack) {
      if (busy(stack)) return false;

      final Controller prev = stack.peek();

      int i = 0;
      boolean found = false;

      for (Controller one : stack) {
        if (one == next) {
          found = true;
          break;
        }
        i++;
      }

      if (!found) {
        throw new IllegalArgumentException("Controller is not in stack");
      }

      if (next == null || prev != null && prev.beforeChanged(next)) {
        return false;
      }

      final int depth = i;

      stack.transaction(new RouterStack.TransactionBlock<Controller>() {
        @Override public void run(RouterStack.Transaction<Controller> transaction) {
          transaction.pop(depth);
          applyTransition(containerId, next);
        }
      });

      return true;
    }
  }

  public static class Replace extends FragmentTransition {
    final Controller next;

    public Replace(int containerId, Activity activity,
        FragmentManager fragmentManager, int enter, int exit, Controller next) {
      super(containerId, activity, fragmentManager, enter, exit);
      this.next = next;
    }

    @Override
    public boolean run(RouterStack<Controller> stack) {
      if (busy(stack)) return false;

      final Controller prev = stack.peek();

      if (prev != null && prev.beforeChanged(next)) {
        return false;
      }

      stack.transaction(new RouterStack.TransactionBlock<Controller>() {
        @Override
        public void run(RouterStack.Transaction<Controller> transaction) {
          if (prev != null) {
            transaction.pop();
          }
          transaction.add(next);
          applyTransition(containerId, next);
        }
      });

      return true;
    }
  }

  public static class Clear extends FragmentTransition {
    final Controller next;

    public Clear(int containerId, Activity activity,
        FragmentManager fragmentManager, int enter, int exit, Controller next) {
      super(containerId, activity, fragmentManager, enter, exit);
      this.next = next;
    }

    @Override
    public boolean run(final RouterStack<Controller> stack) {
      if (busy(stack)) return false;

      final Controller prev = stack.peek();

      if (prev != null && prev.beforeChanged(next)) {
        return false;
      }

      stack.transaction(new RouterStack.TransactionBlock<Controller>() {
        @Override public void run(RouterStack.Transaction<Controller> transaction) {
          // pop all controllers
          transaction.pop(stack.size());
          // attach new controller
          transaction.add(next);
          applyTransition(containerId, next);
        }
      });

      return true;
    }
  }

  /**
   * Internal transition
   */
  public static class Render extends FragmentTransition {

    final Controller next;

    public Render(int containerId, Activity activity,
        FragmentManager fragmentManager, Controller next) {
      super(containerId, activity, fragmentManager, 0, 0);
      this.next = next;
    }

    @Override
    public boolean run(final RouterStack<Controller> stack) {
      stack.transaction(new RouterStack.TransactionBlock<Controller>() {
        @Override public void run(RouterStack.Transaction<Controller> transaction) {
          applyTransition(containerId, next);
        }
      });
      return true;
    }
  }
}
