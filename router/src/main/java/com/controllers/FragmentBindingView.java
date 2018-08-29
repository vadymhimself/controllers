package com.controllers;

import android.annotation.SuppressLint;
import android.database.Observable;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.controllers.Controller.ViewLifecycleConsumer;
import com.controllers.Controller.ViewLifecycleEvent;
import java.lang.reflect.Field;

/**
 * Fragment-based BindingView
 *
 * Fragments are never recreated by Android, state is saved through
 * Controller upon recreation of activity.
 *
 * TODO: replace with simple ViewGroup
 * Created by Vadym Ovcharenko
 * 27.11.2016.
 */

@SuppressLint("ValidFragment") // Fragments are never recreated by Android
public final class FragmentBindingView<B extends ViewDataBinding> extends Fragment
    implements View.OnAttachStateChangeListener, Controller.BindingView<B> {

    @NonNull
    final Controller<B> controller;
    @Nullable
    B binding;

    private final ReplaySubject subject = new ReplaySubject();


    FragmentBindingView(@NonNull Controller<B> c) {
        this.controller = c;
    }

    @SuppressWarnings(value = {"ConstantConditions", "unchecked"})
    @Nullable @Override public View onCreateView(@NonNull LayoutInflater inflater,
                                                 @Nullable ViewGroup parent,
                                                 @Nullable Bundle savedInstanceState) {
        if (controller == null) {
            throw new IllegalStateException();
        }

        int layoutRes = controller.getLayoutId();
        if (layoutRes == 0) {
            throw new IllegalStateException(controller.getClass().getSimpleName() + " has 0x0 layout resource ID");
        }

        if (binding != null) {
            // if view was retained
            try {
                // clear fucking parent reference (because SDK fails to do it)
                Field f = View.class.getDeclaredField("mParent"); // TODO reflection proguard rule
                f.setAccessible(true);
                f.set(binding.getRoot(), null);
                return binding.getRoot();
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
                // failed reflection
            }
        }

        binding = DataBindingUtil.inflate(inflater, layoutRes, parent, false);

        if (binding == null) {
            throw new IllegalStateException(
                    "Failed to inflate layout for " +
                            controller.getClass().getSimpleName() +
                            ". Make sure to declare binding <layout> in xml."
            );
        }

        binding.getRoot().addOnAttachStateChangeListener(this);

        // inject controller
        binding.setVariable(BR.controller, controller);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        subject.notifyObservers(ViewLifecycleEvent.CREATE);
    }

    @Override public void onViewAttachedToWindow(View v) {
        controller.onAttachedToScreen(this);
    }

    @Override public void onViewDetachedFromWindow(View v) {
        controller.onDetachedFromScreen(this);
        v.removeOnAttachStateChangeListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        logi("onStart");
        subject.notifyObservers(ViewLifecycleEvent.START);
    }

    @Override
    public void onResume() {
        super.onResume();
        logi("onResume");
        subject.notifyObservers(ViewLifecycleEvent.RESUME);
    }

    @Override
    public void onPause() {
        super.onPause();
        logi("onPause");
        subject.notifyObservers(ViewLifecycleEvent.PAUSE);
    }

    @Override
    public void onStop() {
        super.onStop();
        logi("onStop");
        subject.notifyObservers(ViewLifecycleEvent.STOP);
    }

    @SuppressWarnings("ConstantConditions")
    @Override public void onDestroyView() {
        super.onDestroyView();
        logi("onDestroyView");

        if (controller.shouldRetainView()) {
            // keep alive
            return;
        }

        if (binding != null) {
            binding.unbind();
            binding = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        logi("onDestroy");

        if (binding != null) {
            binding = null;
        }

        subject.notifyObservers(ViewLifecycleEvent.DESTROY);
        subject.unregisterAll();
    }

    @Nullable
    @Override
    public final B getBinding() {
        return binding;
    }

    public void subscribe(ViewLifecycleConsumer consumer) {
        subject.registerObserver(consumer);
    }

    public void unsubscribe(ViewLifecycleConsumer consumer) {
        subject.unregisterObserver(consumer);
    }

    private void logi(String msg) {
        controller.getLogger().logi(getClass().getSimpleName(), msg);
    }

    static class ReplaySubject extends Observable<ViewLifecycleConsumer> {

        private int index = -1;

        void notifyObservers(ViewLifecycleEvent event) {
            for (ViewLifecycleConsumer consumer : mObservers) {
                deliverEvent(event, consumer);
            }
            this.index = event.order;
        }

        private void deliverEvent(ViewLifecycleEvent event,
            ViewLifecycleConsumer consumer) {
            switch (event) {
                case CREATE:
                    // we never save fragment state to bundle
                    consumer.onCreate(null);
                    break;
                case START:
                    consumer.onStart();
                    break;
                case RESUME:
                    consumer.onResume();
                    break;
                case PAUSE:
                    consumer.onPause();
                    break;
                case STOP:
                    consumer.onStop();
                    break;
                case DESTROY:
                    consumer.onDestroy();
                    break;
            }
        }

        private void replayEvents(ViewLifecycleConsumer consumer) {
            for (ViewLifecycleEvent event : ViewLifecycleEvent.values()) {
                if (event.order <= index) {
                    deliverEvent(event, consumer);
                }
            }
        }

        @Override
        public void registerObserver(ViewLifecycleConsumer consumer) {
            super.registerObserver(consumer);
            replayEvents(consumer);
        }
    }
}