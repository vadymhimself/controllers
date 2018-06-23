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
import android.util.Log;
import java.util.Iterator;

/**
 * Created by Vadym Ovcharenko
 * 18.10.2016.
 */

public abstract class ControllerActivity extends AppCompatActivity implements Router, Host {

    private static final String TAG = ControllerActivity.class.getSimpleName();
    private static final String KEY_STACK = "_controller_stack";
    private static final String KEY_CONTAINER_ID = "_container_id";

    protected RouterStack<Controller> stack;

    private @IdRes int containerId;
    private final Handler handler = new Handler(Looper.getMainLooper());

    protected void setControllerContainer(@IdRes int containerResId) {
        this.containerId = containerResId;
    }

    @Override
    public boolean show(@NonNull final Controller next,
                           @AnimRes final int enter, @AnimRes final int exit) {
        if (stack.isInTransaction() || isFinishing()) {
            // TODO: logger
            Log.w(TAG, "show: ignored call for router in transaction");
            return false;
        }

        final Controller prev = stack.peek();
        if (beforeControllersChanged(prev, next) || prev != null && prev.beforeChanged(next)) {
            return false;
        }

        stack.beginTransaction(new RouterStack.TransactionBlock<Controller>() {
            @Override public void run(RouterStack.Transaction<Controller> transaction) {

                transaction.add(next);
                next.onAttachedToStackInternal(ControllerActivity.this);

                applyTransaction(prev, next, enter, exit, false, transaction);
            }
        });

        return true;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean back(@AnimRes final int enter, @AnimRes final int exit) {

        if (stack.isInTransaction() || isFinishing()) {
            // TODO: logger
            Log.w(TAG, "back: ignored call for router in transaction");
            return false;
        }

        if (stack.size() <= 1) throw new IllegalStateException("Stack must be bigger than 1");

        final Controller prev = stack.peek();
        final Controller next = stack.peek(1);

        if (beforeControllersChanged(prev, next) || prev != null && prev.beforeChanged(next)) {
            return false;
        }

        stack.beginTransaction(new RouterStack.TransactionBlock<Controller>() {
            @Override public void run(RouterStack.Transaction<Controller> transaction) {
                transaction.pop();
                prev.onDetachedFromStackInternal();
                applyTransaction(prev, next, enter, exit, false, transaction);
            }
        });

        return true;
    }

    @Override
    public boolean goBackTo(Controller controller, @AnimRes final int enter, @AnimRes final int exit) {

        if (stack.isInTransaction() || isFinishing()) {
            // TODO: logger
            Log.w(TAG, "goBackTo: ignored call for router in transaction");
            return false;
        }

        final Controller prev = stack.peek();
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
            return false;
        }

        final int depth = i;
        final Controller finalNext = next;

        stack.beginTransaction(new RouterStack.TransactionBlock<Controller>() {
            @Override public void run(RouterStack.Transaction<Controller> transaction) {
                for (Controller c : transaction.pop(depth)) {
                    c.onDetachedFromStackInternal();
                }

                applyTransaction(prev, finalNext, enter, exit, false, transaction);
            }
        });

        return true;
    }

    @Override
    public boolean replace(final Controller next,
                              @AnimRes final int enter, @AnimRes final int exit) {

        if (stack.isInTransaction() || isFinishing()) {
            // TODO: logger
            Log.w(TAG, "replace: ignored call for router in transaction");
            return false;
        }

        final Controller prev = stack.peek();

        if (beforeControllersChanged(prev, next) ||
                prev != null && prev.beforeChanged(next)) {
            return false;
        }

        stack.beginTransaction(new RouterStack.TransactionBlock<Controller>() {
            @Override public void run(RouterStack.Transaction<Controller> transaction) {
                if (prev != null) {
                    transaction.pop();
                    prev.onDetachedFromStackInternal();
                }

                transaction.add(next);
                next.onAttachedToStackInternal(ControllerActivity.this);

                applyTransaction(prev, next, enter, exit, false, transaction);
            }
        });

        return true;
    }

    @Override
    public boolean clear(final Controller next, @AnimRes final int enter, @AnimRes final int exit) {

        if (stack.isInTransaction() || isFinishing()) {
            // TODO: logger
            Log.w(TAG, "clear: ignored call for router in transaction");
            return false;
        }

        final Controller prev = stack.peek();

        if (beforeControllersChanged(prev, next) ||
                prev != null && prev.beforeChanged(next)) {
            return false;
        }

        stack.beginTransaction(new RouterStack.TransactionBlock<Controller>() {
            @Override public void run(RouterStack.Transaction<Controller> transaction) {
                for (Controller c : transaction.pop(stack.size())) {
                    // pop and detach all controllers
                    c.onDetachedFromStackInternal();
                }

                // attach new controller
                transaction.add(next);
                next.onAttachedToStackInternal(ControllerActivity.this);

                applyTransaction(prev, next, enter, exit, false, transaction);
            }
        });

        return true;
    }

    private void applyTransaction(@Nullable Controller prev,
                                    final Controller next,
                                    @AnimRes int enter,
                                    @AnimRes int exit,
                                    boolean immediate,
                                    final RouterStack.Transaction<Controller> stackTransaction) {

        @SuppressLint("CommitTransaction")
        FragmentTransaction fragTrans = getSupportFragmentManager().beginTransaction();

        if (enter != 0 || exit != 0) {
            fragTrans = fragTrans.setCustomAnimations(enter, exit);
        }

        final FragmentTransaction t = fragTrans.
            replace(containerId, next.asFragment(), next.getTag().toString());

        Runnable r = new Runnable() {
            @Override public void run() {
                // TODO: check for state loss
                try {
                    t.commitNow();
                    stackTransaction.commit();
                } catch (Throwable t) {
                    stackTransaction.rollBack();
                    throw new RuntimeException("Transaction failed with rollback", t);
                }
                onControllerChanged(next);
            }
        };

        if (immediate) {
            r.run();
        } else {
            handler.post(r);
        }
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
            final Controller controller = stack.peek();
            if (controller == null) throw new IllegalStateException();

            stack.beginTransaction(new RouterStack.TransactionBlock<Controller>() {
                @Override public void run(RouterStack.Transaction<Controller> transaction) {
                    applyTransaction(null, controller, 0, 0, true, transaction);
                }
            });
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
    public boolean show(@NonNull Controller controller) {
        return show(controller, R.anim.slide_in_right, R.anim.slide_out_left);
    }

    /**
     * Pops top controller with default animation
     */
    @Override
    public boolean back() {
        return back(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    /**
     * Backs to given controller with default animation
     */
    @Override
    public boolean goBackTo(Controller controller) {
        return goBackTo(controller, R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public boolean replace(Controller controller) {
        return replace(controller, android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public boolean clear(Controller controller) {
        return clear(controller, R.anim.fade_in_short, R.anim.fade_out_short);
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
