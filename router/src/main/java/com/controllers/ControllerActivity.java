package com.controllers;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.AnimRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import java.util.Iterator;

/**
 * Created by Vadym Ovcharenko
 * 18.10.2016.
 */

public abstract class ControllerActivity extends AppCompatActivity implements Router, Host {

    private static final String KEY_STACK = "_controller_stack";
    private static final String KEY_CONTAINER_ID = "_container_id";

    protected RouterStack<Controller> stack;

    private @IdRes int containerId;
    private final Handler handler = new Handler(Looper.getMainLooper());

    protected void setControllerContainer(@IdRes int containerResId) {
        this.containerId = containerResId;
    }

    @Override
    @Nullable
    public Controller show(@NonNull Controller next,
                           @AnimRes int enter, @AnimRes int exit) {

        Controller prev = stack.peek();
        if (beforeControllersChanged(prev, next) || prev != null && prev.beforeChanged(next)) {
            return null;
        }

        stack.add(next);
        next.onAttachedToStackInternal(this);

        return changeControllersInternal(prev, next, enter, exit);
    }

    private void restore(@NonNull Controller controller) {
        changeControllersInternal(null, controller, 0, 0);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    @Nullable
    public Controller back(@AnimRes int enter, @AnimRes int exit) {
        if (stack.size() <= 1) throw new IllegalStateException("Stack must be bigger than 1");

        Controller prev = stack.peek();
        Controller next = stack.peek(1);

        if (beforeControllersChanged(prev, next) || prev != null && prev.beforeChanged(next)) {
            return null;
        }

        stack.pop();
        prev.onDetachedFromStackInternal();

        return changeControllersInternal(prev, next, enter, exit);
    }

    @Override
    @Nullable
    public Controller goBackTo(Controller controller, @AnimRes int enter, @AnimRes int exit) {
        Controller prev = stack.peek();
        Controller next = null;

        int i = 0;
        boolean found = false;

        for (Controller one : stack) {
            if (one == controller) {
                next = one;
                found = true;
                break;
            }
            i++;
        }

        if (!found) {
            throw new IllegalArgumentException("Controller is not in stack");
        }

        if (next == null || beforeControllersChanged(prev, next) ||
                prev != null && prev.beforeChanged(next)) {
            return null;
        }

        for (Controller c : stack.pop(i)) {
            c.onDetachedFromStackInternal();
        }

        return changeControllersInternal(prev, next, enter, exit);
    }

    @Override
    @Nullable
    public Controller replace(Controller next,
                              @AnimRes int enter, @AnimRes int exit) {
        Controller prev = stack.peek();

        if (beforeControllersChanged(prev, next) ||
                prev != null && prev.beforeChanged(next)) {
            return null;
        }

        if (prev != null) {
            stack.pop();
            prev.onDetachedFromStackInternal();
        }

        stack.add(next);
        next.onAttachedToStackInternal(this);

        return changeControllersInternal(prev, next, enter, exit);
    }

    @Nullable
    @Override
    public Controller clear(Controller controller) {
        return clear(controller, R.anim.fade_in_short, R.anim.fade_out_short);
    }

    @Nullable
    @Override
    public Controller clear(Controller next, @AnimRes int enter, @AnimRes int exit) {
        Controller prev = stack.peek();

        if (beforeControllersChanged(prev, next) ||
                prev != null && prev.beforeChanged(next)) {
            return null;
        }

        for (Controller c : stack.pop(stack.size())) {
            // pop and detach all controllers
            c.onDetachedFromStackInternal();
        }

        // attach new controller
        stack.add(next);
        next.onAttachedToStackInternal(this);

        return changeControllersInternal(prev, next, enter, exit);
    }

    private Controller changeControllersInternal(@Nullable Controller prev,
                                                 final Controller next,
                                                 @AnimRes int enter,
                                                 @AnimRes int exit) {

        @SuppressLint("CommitTransaction")
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (enter != 0 || exit != 0) {
            transaction = transaction.setCustomAnimations(enter, exit);
        }

        final FragmentTransaction capturedTransaction = transaction.
            replace(containerId, next.asFragment(), next.getTag().toString());

        handler.post(new Runnable() {
            @Override public void run() {
                // TODO: check for state loss
                capturedTransaction.commitNow();
                onControllerChanged(next);
            }
        });

        return prev;
    }

    @Override
    @Nullable
    public <T> T findByClass(Class<T> clazz) {
        if (stack == null) throw new IllegalStateException();
        for (Controller controller : stack) {
            if (clazz.isAssignableFrom(controller.getClass())) {
                return clazz.cast(controller);
            }
        }
        return null;
    }

    @Override
    @Nullable
    public Controller findByTag(Object tag) {
        if (stack == null) throw new IllegalStateException();
        for (Controller controller : stack) {
            if (controller.getTag().equals(tag)) {
                return controller;
            }
        }
        return null;
    }

    /**
     *
     * This method can be used to prevent controllers change.
     * TODO: unit-tests
     * @return true if want to override controllers change.
     */
    protected boolean beforeControllersChanged(Controller previous, Controller next) {
        return false; // stub
    }

    protected void onControllerChanged(Controller controller) {

    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(noFragmentsRestore(savedInstanceState));
        if (savedInstanceState != null) {
            this.containerId = savedInstanceState.getInt(KEY_CONTAINER_ID);
            this.stack = (RouterStack) savedInstanceState.getSerializable(KEY_STACK);
            if (stack == null) {
                throw new IllegalStateException("Stack should be saved");
            }

            for (Controller controller : stack) {
                controller.onAttachedToStackInternal(this);
                controller.onRestoredInternal();
            }
        } else {
            stack = new RouterStack<>();
        }
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (savedInstanceState != null) {
            // restore top controller after recreation
            Controller controller = stack.peek();
            if (controller == null) throw new IllegalStateException();
            restore(controller);
        }
    }

    /**
     * Improve bundle to prevent restoring of fragments.
     *
     * @param bundle bundle container
     * @return improved bundle with removed "fragments parcelable"
     */
    private static Bundle noFragmentsRestore(Bundle bundle) {
        if (bundle != null) {
            bundle.remove("android:support:fragments");
        }
        return bundle;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (isChangingConfigurations() || isFinishing()) {
            // activity is about to die
            for (Controller controller : stack) {
                controller.onDetachedFromScreenInternal();
            }
        }
        outState.putInt(KEY_CONTAINER_ID, containerId);
        outState.putSerializable(KEY_STACK, stack);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (Controller controller : stack) {
            controller.onDetachedFromStackInternal();
        }
    }

    /**
     * Shows controller with default animation
     */
    @Override
    @Nullable
    public Controller show(@NonNull Controller controller) {
        return show(controller, R.anim.slide_in_right, R.anim.slide_out_left);
    }

    /**
     * Pops top controller with default animation
     */
    @Override
    @Nullable
    public Controller back() {
        return back(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    /**
     * Backs to given controller with default animation
     */
    @Override
    @Nullable
    public Controller goBackTo(Controller controller) {
        return goBackTo(controller, R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    @Nullable
    public Controller replace(Controller controller) {
        return replace(controller, android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Nullable
    @Override
    public Controller getTop() {
        if (stack != null) {
            return stack.peek();
        } else {
            return null;
        }
    }

    @Nullable
    @Override
    public Controller getPrevious() {
        if (stack != null) {
            return stack.peek(1);
        } else {
            return null;
        }
    }

    @Nullable
    @Override
    public Controller getBottom() {
        if (stack != null) {
            return stack.peek(stack.size() - 1);
        } else {
            return null;
        }
    }

    @NonNull
    @Override
    public Iterator<Controller> iterator() {
        return stack.iterator();
    }

    @SuppressWarnings("ConstantConditions")
    @Override public void onBackPressed() {
        if (stack == null || stack.size() == 0 || !stack.peek().onBackPressed()) {
            super.onBackPressed();
        }
    }
}
