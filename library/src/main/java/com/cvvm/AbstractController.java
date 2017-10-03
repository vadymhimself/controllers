package com.cvvm;

import android.databinding.BaseObservable;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;


/**
 * Created by Vadim Ovcharenko
 * 18.10.2016.
 */

public abstract class AbstractController<B extends ViewDataBinding> extends BaseObservable implements IController, ExecutionContext {

    private transient Strategy<B> strategy;
    private transient boolean isAttached;

    // must be public with no arguments
    public AbstractController() {
        strategy = createStrategy();
        if (strategy == null) throw new IllegalStateException();
    }

    abstract Strategy<B> createStrategy();

    @Nullable public ControllerActivity getActivity() {
        return strategy.getActivity();
    }

    @NonNull @Override public final Fragment asFragment() {
        return strategy.asFragment();
    }

    public final void subscribe(FragmentObserver observer) {
        strategy.subscribe(observer);
    }

    void onAttached() {
        isAttached = true;
    }

    void onDetached() {
        isAttached = false;
    }

    void onRestored() {

    }

    boolean isAttached() {
        return isAttached;
    }

    @Nullable protected B getBinding() {
        return strategy.getBinding();
    }

    /**
     * @return true if want to override default behaviour
     */
    protected boolean onBackPressed() {
        AbstractControllerActivity activity = getActivity();
        if (activity != null) {
            if (activity.stack != null && activity.stack.size() > 1) {
                activity.back();
                return true;
            }
        }
        return false;
    }

    public final void show(AbstractController controller) {
        if (getActivity() != null) {
            getActivity().show(controller);
        }
    }

    public final void show(@NonNull AbstractController next,
                           @AnimRes int enter, @AnimRes int exit){
        if (getActivity() != null) {
            getActivity().show(next, enter, exit);
        }
    }

    public final void back() {
        if (isAttached && getActivity() != null) {
            getActivity().back();
        }
    }

    public final void back(@AnimRes int enter, @AnimRes int exit) {
        if (isAttached && getActivity() != null) {
            getActivity().back(enter, exit);
        }
    }


    public final void replace(AbstractController controller) {
        if (getActivity() != null) {
            getActivity().replace(controller);
        }
    }

    protected final void goBackTo(AbstractController controller) {
        if (getActivity() != null) {
            getActivity().goBackTo(controller);
        }
    }

    // go back with custom
    protected final void goBackTo(AbstractController controller, @AnimRes int enter, @AnimRes int exit) {
        if (getActivity() != null) {
            getActivity().goBackTo(controller, enter, exit);
        }
    }

    @Nullable
    protected final <T extends Controller> T findByClass(Class<T> clazz) {
        if (getActivity() != null) {
            return getActivity().findByClass(clazz);
        } else {
            return null;
        }
    }

    @Nullable
    protected final AbstractController findByTag(String tag) {
        if (getActivity() != null) {
            return getActivity().findByTag(tag);
        } else {
            return null;
        }
    }

    @Nullable
    public Controller getPrevious() {
        if (getActivity() != null && getActivity().stack.size() > 1) {
            return (Controller) getActivity().stack.peek(1);
        } else {
            return null;
        }
    }

    public String getTitle() {
        return "Untitled controller";
    }

    boolean beforeChanged(AbstractController next) {
        return false;
    }

    public final <T> RequestBuilder<T> getRequestBuilder(Class<T> service) {
        return new RequestBuilderImpl<>(this, service);
    }

    // result will be returned to the calling controller
    protected void requestPermission(String permission, PermissionListener callback) {
        if (getActivity() != null) {
            getActivity().requestPermission(permission, callback);
        }
    }

    /**
     * Strategy of the Controller representation in terms of Android
     */
    interface Strategy<B extends ViewDataBinding> {
        ControllerActivity getActivity();
        Fragment asFragment();
        void subscribe(FragmentObserver observer);
        B getBinding();
    }

    public interface FragmentObserver {
        void onCreate(Bundle savedState);
        void onStart();
        void onResume();
        void onPause();
        void onStop();
        void onDestroy();
    }
}
