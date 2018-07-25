package com.controllers;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;


/**
 * Created by Vadym Ovcharenko
 * 18.10.2016.
 *
 * Lifecycle contract: stack lifecycle does not depend on the screen lifecycle. A controller
 * may stay on screen even after it was detached from stack (e.g. for ending transitions). It may
 * also be attached to screen before stack in some cases.
 */

public abstract class AbstractController<B extends ViewDataBinding> extends
        BaseObservable implements IController, Router {

    public final String TAG = Const.LOG_PREFIX + getClass().getSimpleName();

    /**
     * ViewStrategy of the Controller representation in terms of Android
     */
    interface ViewStrategy<B extends ViewDataBinding> {
        ControllerActivity getActivity();
        Fragment asFragment();
        B getBinding();
        void subscribe(ViewLifecycleConsumer consumer);
        void unsubscribe(ViewLifecycleConsumer consumer);
    }

    /**
     * TODO: document use cases. Example: MapView
     */
    public interface ViewLifecycleConsumer {
        void onCreate(Bundle var1);
        void onStart();
        void onResume();
        void onPause();
        void onStop();
        void onDestroy();
    }

    enum ViewLifecycleEvent {
        CREATE(0), START(1), RESUME(2), PAUSE(3), STOP(4), DESTROY(5);

        int order;

        ViewLifecycleEvent(int order) {
            this.order = order;
        }
    }

    @Nullable private Router router;
    @Nullable private View view;

    private ViewStrategy<B> viewStrategy;

    private boolean attachedToScreen;
    private boolean attachedToStack;

    // must be public with no arguments
    public AbstractController() {
        viewStrategy = createStrategy();
        if (viewStrategy == null) throw new IllegalStateException();
    }

    abstract ViewStrategy<B> createStrategy();

    protected abstract @LayoutRes int getLayoutId();

    @Nullable
    public final Activity getActivity() {
        if (view != null) {
            return ((InnerFragment) view).getActivity(); // TODO: AndroidView
        }
        return null;
    }

    @Nullable
    @Override
    public final Router getRouter() {
        return router;
    }

    @Nullable
    @Override
    public View getView() {
        return view;
    }

    @NonNull
    public final Fragment asFragment() {
        return viewStrategy.asFragment();
    }

    @Override
    public void onAttachedToScreen(View view) {
        if (attachedToScreen) throwIllegalState("already attached");
        Log.d(TAG, "onAttachedToScreen: ");
        this.view = view;
        attachedToScreen = true;
    }

    @Override
    public void onDetachedFromScreen(View view) {
        if (!attachedToScreen) throwIllegalState("already detached");
        Log.d(TAG, "onDetachedFromScreen: ");
        this.view = null;
        attachedToScreen = false;
    }

    @Override
    public void onAttachedToStack(@NonNull Router router) {
        if (attachedToStack) throwIllegalState("already attached");
        Log.d(TAG, "onAttachedToStack: ");
        this.router = router;
        attachedToStack = true;
    }

    @Override
    public void onDetachedFromStack(@NonNull Router router) {
        if (!attachedToStack) throwIllegalState("already detached");
        Log.d(TAG, "onDetachedFromStack: ");
        attachedToStack = false;
        this.router = null;
    }

    void onRestoredInternal() {
        if (attachedToScreen || !attachedToStack) throw new IllegalStateException();
        onRestored();
    }

    /**
     * This method is called when the controllers stack was restored after the
     * activity recreation.
     *.
     */
    void onRestored() {

    }

    boolean isAttachedToScreen() {
        return attachedToScreen;
    }

    boolean isAttachedToStack() {
        return attachedToStack;
    }

    @Nullable
    protected B getBinding() {
        return viewStrategy.getBinding();
    }

    /**
     * @return true if want to override default behaviour
     */
    protected boolean onBackPressed() {
        back();
        return true;
    }

    @Override
    public final boolean show(Controller controller) {
        if (attachedToScreen && router != null) {
            return router.show(controller);
        } else {
            Log.w(TAG, "show: ignored call from detached controller");
            return false;
        }
    }

    @Override
    public final boolean show(@NonNull Controller next,
                           @AnimRes int enter, @AnimRes int exit) {
        if (attachedToScreen && router != null) {
            return router.show(next, enter, exit);
        } else {
            Log.w(TAG, "show: ignored call from detached controller");
            return false;
        }
    }

    @Override
    public final boolean back() {
        if (attachedToScreen && router != null) {
            return router.back();
        } else {
            Log.w(TAG, "back: ignored call from detached controller");
            return false;
        }
    }

    @Override
    public final boolean back(@AnimRes int enter, @AnimRes int exit) {
        if (attachedToScreen && router != null) {
            return router.back(enter, exit);
        } else {
            Log.w(TAG, "back: ignored call from detached controller");
            return false;
        }
    }

    @Override
    public final boolean replace(Controller controller, @AnimRes int enter, @AnimRes int exit) {
        // TODO: should pop all the controllers laying above it before replace
        if (attachedToScreen && router != null) {
            return router.replace(controller, enter, exit);
        } else {
            Log.w(TAG, "replace: ignored call from detached controller");
            return false;
        }
    }

    @Override
    public final boolean replace(Controller controller) {
        if (attachedToScreen && router != null) {
            return router.replace(controller);
        } else {
            Log.w(TAG, "replace: ignored call from detached controller");
            return false;
        }
    }

    @Override
    public boolean clear(Controller controller) {
        if (attachedToScreen && router != null) {
            return router.clear(controller);
        } else {
            Log.w(TAG, "clear: ignored call from detached controller");
            return false;
        }
    }

    @Override
    public boolean clear(Controller controller, int enter, int exit) {
        if (attachedToScreen && router != null) {
            return router.clear(controller, enter, exit);
        } else {
            Log.w(TAG, "clear: ignored call from detached controller");
            return false;
        }
    }

    @Override
    public final boolean goBackTo(Controller controller) {
        if (attachedToScreen && router != null) {
            return router.goBackTo(controller);
        } else {
            Log.w(TAG, "goBackTo: ignored call from detached controller");
            return false;
        }
    }

    @Override
    public final boolean goBackTo(Controller controller,
                               @AnimRes int enter, @AnimRes int exit) {
        if (attachedToScreen && router != null) {
            return router.goBackTo(controller, enter, exit);
        } else {
            Log.w(TAG, "goBackTo: ignored call from detached controller");
            return false;
        }
    }

    @Override
    @Nullable
    public final <T> T findByClass(Class<T> clazz) {
        if (router != null) {
            return router.findByClass(clazz);
        } else {
            return null;
        }
    }

    @Override
    @Nullable
    public final Controller findByTag(Object tag) {
        if (router != null) {
            return router.findByTag(tag);
        } else {
            return null;
        }
    }

    @Override
    @Nullable
    public Controller getPrevious() {
        if (router != null) {
            return router.getPrevious();
        } else {
            return null;
        }
    }

    @Nullable
    @Override
    public Controller getTop() {
        if (router != null) {
            return router.getTop();
        } else {
            return null;
        }
    }

    @Nullable
    @Override
    public Controller getBottom() {
        if (router != null) {
            return router.getBottom();
        } else {
            return null;
        }
    }

    // TODO: I don't really like it
    public String getTitle() {
        return "Untitled controller";
    }

    @Deprecated
    protected boolean shouldRetainView() {
        return false;
    }

    boolean beforeChanged(AbstractController next) {
        return false;
    }

    void subscribe(ViewLifecycleConsumer consumer) {
        viewStrategy.subscribe(consumer);
    }

    void unsubscribe(ViewLifecycleConsumer consumer) {
        viewStrategy.unsubscribe(consumer);
    }

    private void throwIllegalState(String message) {
        throw new IllegalStateException(getClass().getSimpleName() + ": " + message);
    }

    public void logd(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG + "/" + tag, msg);
        }
    }
}
