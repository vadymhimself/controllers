package com.controllers;

import android.databinding.BaseObservable;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;


/**
 * Created by Vadym Ovcharenko
 * 18.10.2016.
 */

public abstract class AbstractController<B extends ViewDataBinding> extends
        BaseObservable implements IController {

    public static final String TAG = AbstractController.class.getSimpleName();

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

    public interface ViewLifecycleConsumer {
        void onCreate(Bundle var1);
        void onStart();
        void onResume();
        void onPause();
        void onStop();
        void onDestroy();
    }

    private transient ControllerActivity host;

    private transient ViewStrategy<B> viewStrategy;
    private transient boolean attachedToScreen;
    private transient boolean retained;

    private boolean attachedToStack;

    // must be public with no arguments
    public AbstractController() {
        viewStrategy = createStrategy();
        if (viewStrategy == null) throw new IllegalStateException();
    }

    abstract ViewStrategy<B> createStrategy();

    @Nullable
    public ControllerActivity getActivity() {
        return host;
    }

    @NonNull
    @Override
    public final Fragment asFragment() {
        return viewStrategy.asFragment();
    }

    void onAttachedToScreenInternal() {
        if (attachedToScreen) throw new IllegalStateException();
        attachedToScreen = true;
    }

    void onDetachedFromScreenInternal() {
        if (!attachedToScreen) throw new IllegalStateException();
        attachedToScreen = false;
    }

    void onAttachedToStackInternal(ControllerActivity host) {
        if (attachedToStack || host == null) throw new IllegalStateException();
        this.host = host;
        attachedToStack = true;
        onAttachedToStack();
    }

    void onDetachedFromStackInternal() {
        if (!attachedToStack) throw new IllegalStateException();
        attachedToStack = false;
        onDetachedFromStack();
        this.host = null;
    }

    void onRestoredInternal() {
        if (attachedToScreen || !attachedToStack) throw new IllegalStateException();
        // check if was deserialized
        onRestored(!retained);
        retained = true;
    }

    protected void onAttachedToStack() {

    }

    protected void onDetachedFromStack() {

    }

    /**
     * This method is called when the controllers stack was restored after the
     * activity recreation.
     *
     * @param deserialized true if the controller was deserialized during the
     *                     recreation.
     */
    void onRestored(boolean deserialized) {

    }

    boolean isAttachedToScreen() {
        return attachedToScreen;
    }

    @Nullable
    protected B getBinding() {
        return viewStrategy.getBinding();
    }

    /**
     * @return true if want to override default behaviour
     */
    protected boolean onBackPressed() {
        ControllerActivity activity = getActivity();
        if (activity != null) {
            if (activity.stack != null && activity.stack.size() > 1) {
                activity.back();
                return true;
            }
        }
        return false;
    }

    @Override
    public final void show(Controller controller) {
        if (attachedToScreen && getActivity() != null && !getActivity()
                .isFinishing()) {
            getActivity().show(controller);
        } else {
            Log.w(TAG, "show: ignored call from detached controller");
        }
    }

    @Override
    public final void show(@NonNull Controller next,
                           @AnimRes int enter, @AnimRes int exit) {
        if (attachedToScreen && getActivity() != null && !getActivity()
                .isFinishing()) {
            getActivity().show(next, enter, exit);
        } else {
            Log.w(TAG, "show: ignored call from detached controller");
        }
    }

    @Override
    public final void back() {
        if (attachedToScreen && getActivity() != null && !getActivity()
                .isFinishing()) {
            getActivity().back();
        } else {
            Log.w(TAG, "back: ignored call from detached controller");
        }
    }

    @Override
    public final void back(@AnimRes int enter, @AnimRes int exit) {
        if (attachedToScreen && getActivity() != null && !getActivity()
                .isFinishing()) {
            getActivity().back(enter, exit);
        } else {
            Log.w(TAG, "back: ignored call from detached controller");
        }
    }

    @Override
    public final void replace(Controller controller) {
        if (attachedToScreen && getActivity() != null && !getActivity()
                .isFinishing()) {
            getActivity().replace(controller);
        } else {
            Log.w(TAG, "replace: ignored call from detached controller");
        }
    }

    @Override
    public final void goBackTo(Controller controller) {
        if (attachedToScreen && getActivity() != null && !getActivity()
                .isFinishing()) {
            getActivity().goBackTo(controller);
        } else {
            Log.w(TAG, "goBackTo: ignored call from detached controller");
        }
    }

    @Override
    public final void goBackTo(Controller controller,
                               @AnimRes int enter, @AnimRes int exit) {
        if (attachedToScreen && getActivity() != null && !getActivity()
                .isFinishing()) {
            getActivity().goBackTo(controller, enter, exit);
        } else {
            Log.w(TAG, "goBackTo: ignored call from detached controller");
        }
    }

    @Override
    @Nullable
    public final <T extends Controller> T findByClass(Class<T> clazz) {
        if (getActivity() != null) {
            return getActivity().findByClass(clazz);
        } else {
            return null;
        }
    }

    @Override
    @Nullable
    public final Controller findByTag(String tag) {
        if (getActivity() != null) {
            return getActivity().findByTag(tag);
        } else {
            return null;
        }
    }

    @Override
    @Nullable
    public Controller getPrevious() {
        if (getActivity() != null && getActivity().stack.size() > 1) {
            return getActivity().stack.peek(1);
        } else {
            return null;
        }
    }

    // TODO: I don't really like it
    public String getTitle() {
        return "Untitled controller";
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
}
