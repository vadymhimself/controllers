package com.controllers;

import android.annotation.SuppressLint;
import android.support.annotation.AnimRes;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.controllers.core.Router;
import com.controllers.core.RouterStack;

/**
 * TODO: unit test transitions
 */
public abstract class FragmentTransitions {

  static abstract class FragmentTransition implements Router.Transition<Controller> {

    final @IdRes int containerId;
    final AppCompatActivity activity;

    @AnimRes int enter;
    @AnimRes int exit;

    FragmentTransition(int containerId, AppCompatActivity activity, int enter, int exit) {
      this.containerId = containerId;
      this.activity = activity;
      this.enter = enter;
      this.exit = exit;
    }

    @Override
    public boolean run(RouterStack<Controller> stack) {
      if (stack.isInTransaction() || activity.isFinishing()) {
        // TODO: logger
        Log.w(Const.LOG_PREFIX, "ignored transition call for router in transaction");
        return false;
      }

      return true;
    }

    final void applyTransition(@IdRes int containerId, final Controller<?> next,
        final RouterStack.Transaction<Controller> stackTransaction) {

      @SuppressLint("CommitTransaction")
      FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();

      if (enter != 0 || exit != 0) {
        transaction = transaction.setCustomAnimations(enter, exit);
      }

      FragmentBindingView view = next.getView();
      if (view == null) {
        view = new FragmentBindingView<>(next);
      }

      transaction.replace(containerId, view, next.ID);

      try {
        transaction.commitNow();
        stackTransaction.commit();
      } catch (Throwable t) {
        // roll back stack changes
        stackTransaction.rollBack();

        // TODO: re-render top controller (stupid fragments leave the view in a broken state)
        // do not break the old view unless the new one is inflated when move to views

        // We still throw to crash the current runtime. If it is a coroutine
        // it will fail but not leave the stack in an unpredicted state (rollback)
        throw new RuntimeException("Controller transaction failed", t);
      }

    }
  }

  public static class Show extends FragmentTransition {

    final Controller next;

    public Show(int containerId, ControllerActivity activity, int enter, int exit,
        Controller next) {
      super(containerId, activity, enter, exit);
      this.next = next;
    }

    @Override
    public boolean run(RouterStack<Controller> stack) {
      if (!super.run(stack)) return false;

      stack.transaction(new RouterStack.TransactionBlock<Controller>() {
        @Override public void run(RouterStack.Transaction<Controller> transaction) {
          transaction.add(next);
          applyTransition(containerId, next, transaction);
        }
      });

      return true;
    }
  }

  public static class Back extends FragmentTransition {

    public Back(int containerId, ControllerActivity activity, int enter, int exit) {
      super(containerId, activity, enter, exit);
    }

    @Override
    public boolean run(RouterStack<Controller> stack) {
      if (stack.size() <= 1) throw new IllegalStateException("Stack must be bigger than 1");

      if (!super.run(stack)) return false;

      final Controller prev = stack.peek();
      final Controller next = stack.peek(1);

      if (prev != null && prev.beforeChanged(next)) {
        return false;
      }

      stack.transaction(new RouterStack.TransactionBlock<Controller>() {
        @Override
        public void run(RouterStack.Transaction<Controller> transaction) {
          transaction.pop();
          applyTransition(containerId, next, transaction);
        }
      });

      return true;
    }
  }

  public static class GoBackTo extends FragmentTransition {

    final Controller next;

    public GoBackTo(int containerId, ControllerActivity activity, int enter, int exit,
        Controller next) {
      super(containerId, activity, enter, exit);
      this.next = next;
    }

    @Override
    public boolean run(RouterStack<Controller> stack) {
      if (!super.run(stack)) return false;

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
          applyTransition(containerId, next, transaction);
        }
      });

      return true;
    }
  }

  public static class Replace extends FragmentTransition {
    final Controller next;

    public Replace(int containerId, ControllerActivity activity, int enter, int exit,
        Controller next) {
      super(containerId, activity, enter, exit);
      this.next = next;
    }

    @Override
    public boolean run(RouterStack<Controller> stack) {
      if (!super.run(stack)) return false;

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
          applyTransition(containerId, next, transaction);
        }
      });

      return true;
    }
  }

  public static class Clear extends FragmentTransition {
    final Controller next;

    public Clear(int containerId, ControllerActivity activity, int enter, int exit,
        Controller next) {
      super(containerId, activity, enter, exit);
      this.next = next;
    }

    @Override
    public boolean run(final RouterStack<Controller> stack) {
      if (!super.run(stack)) return false;

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
          applyTransition(containerId, next, transaction);
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

    Render(int containerId, ControllerActivity activity, Controller next) {
      super(containerId, activity, 0, 0);
      this.next = next;
    }

    @Override
    public boolean run(final RouterStack<Controller> stack) {
      stack.transaction(new RouterStack.TransactionBlock<Controller>() {
        @Override public void run(RouterStack.Transaction<Controller> transaction) {
          applyTransition(containerId, next, transaction);
        }
      });
      return true;
    }
  }
}
