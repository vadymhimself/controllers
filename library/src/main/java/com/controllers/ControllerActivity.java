package com.controllers;

import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Vadym Ovcharenko
 * 18.10.2016.
 */

public abstract class ControllerActivity extends AppCompatActivity implements Router {

    private static final String KEY_STACK = "_controller_stack";
    private static final String KEY_CONTAINER_ID = "_container_id";

    protected ControllerStack stack;
    private @IdRes int containerId;

    protected void setControllerContainer(@IdRes int containerResId) {
        this.containerId = containerResId;
    }

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

        return replaceInternal(prev, next, enter, exit);
    }

    @Nullable
    protected Controller replace(Controller next,
                                 @AnimRes int enter, @AnimRes int exit) {
        Controller prev = stack.peek();

        if (beforeControllersChanged(prev, next) ||
                prev != null && prev.beforeChanged(next)) {
            return null;
        }

        return replaceInternal(prev, next, enter, exit);
    }

    // replaces top controller with the new one
    private Controller replaceInternal(Controller prev, Controller next,
                                       @AnimRes int enter, @AnimRes int exit) {
        stack.pop();
        prev.onDetachedFromStackInternal();
        stack.add(next);
        next.onAttachedToStackInternal(this);
        return changeControllersInternal(prev, next, enter, exit);
    }

    private Controller changeControllersInternal(@Nullable Controller prev,
                                                 final Controller next,
                                                 @AnimRes int enter,
                                                 @AnimRes int exit) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (enter != 0 || exit != 0) {
            transaction.setCustomAnimations(enter, exit);
        }

        if (prev != null) prev.onDetachedFromScreenInternal();

        transaction.replace(containerId, next.asFragment(), next.getTag().toString())
                .commitNowAllowingStateLoss();
        next.onAttachedToScreenInternal();
        onControllerChanged(next);
        return prev;
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public  <T extends Controller> T findByClass(Class<T> clazz) {
        if (stack == null) throw new IllegalStateException();
        for (Controller controller : stack) {
            if (controller.getClass() == clazz) {
                return (T) controller;
            }
        }
        return null;
    }

    @Nullable
    public Controller findByTag(Object tag){
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(noFragmentsRestore(savedInstanceState));
        if (savedInstanceState != null) {
            this.containerId = savedInstanceState.getInt(KEY_CONTAINER_ID);
            this.stack = (ControllerStack) savedInstanceState.getSerializable(KEY_STACK);
            if (stack == null) {
                throw new IllegalStateException("Stack should be saved");
            }

            for (Controller controller : stack) {
                controller.onAttachedToStackInternal(this);
                controller.onRestoredInternal();
            }
        } else {
            stack = new ControllerStack();
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
    public Controller show(@NonNull Controller controller) {
        return show(controller, R.anim.slide_in_right, R.anim.slide_out_left);
    }

    /**
     * Pops top controller with default animation
     */
    public Controller back() {
        return back(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    /**
     * Backs to given controller with default animation
     */
    public Controller goBackTo(Controller controller) {
        return goBackTo(controller, R.anim.slide_in_left, R.anim.slide_out_right);
    }

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
            return stack.peek();
        } else {
            return null;
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override public void onBackPressed() {
        if (stack == null || stack.size() == 0 || !stack.peek().onBackPressed()) {
            super.onBackPressed();
        }
    }
}
