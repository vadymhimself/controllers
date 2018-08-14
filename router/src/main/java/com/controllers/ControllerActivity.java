package com.controllers;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.controllers.core.Router;
import com.controllers.core.RouterStack;

/**
 * TODO: change to RouterActivity
 * Created by Vadym Ovcharenko
 * 18.10.2016.
 */

public abstract class ControllerActivity extends AppCompatActivity implements Router<Controller> {

    private static final String KEY_STACK = "_controller_stack";
    private static final String KEY_CONTAINER_ID = "_container_id";

    protected RouterStack<Controller> stack;

    private @IdRes int containerId;

    protected void setControllerContainer(@IdRes int containerResId) {
        this.containerId = containerResId;
    }

    @Override
    public boolean make(Transition<Controller> transition) {
        return transition.run(stack);
    }

    @Override
    @Nullable
    public <C> C findByClass(Class<C> clazz) {
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
        // TODO: optimize with stack index
        for (Controller controller : stack) {
            if (controller.ID.equals(tag)) {
                return controller;
            }
        }
        return null;
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
                controller.onAttachedToRouter();
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
            final Controller controller = stack.peek();
            if (controller == null) throw new IllegalStateException();

            make(new FragmentTransitions.Render(containerId, this, controller));
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
        outState.putInt(KEY_CONTAINER_ID, containerId);
        outState.putSerializable(KEY_STACK, stack);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (Controller controller : stack) {
            controller.onDetachedFromRouter();
        }
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
}
