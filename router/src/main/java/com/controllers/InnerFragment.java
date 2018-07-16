package com.controllers;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.controllers.AbstractController.ViewLifecycleConsumer;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * Representation of the basic Controller in terms of Android.
 *
 * Fragments are never recreated by Android, state is saved through
 * Controller upon recreation of activity.
 *
 * TODO: replace with simple ViewGroup
 * TODO: make sure that onCreate is pushed to the LifecycleConsumers
 * Created by Vadym Ovcharenko
 * 27.11.2016.
 */

@SuppressLint("ValidFragment") // Fragments are never recreated by Android
public final class InnerFragment<B extends ViewDataBinding> extends Fragment
    implements View.OnAttachStateChangeListener, com.controllers.View {

    @NonNull
    final AbstractController controller;
    @Nullable
    B binding;

    private final Set<ViewLifecycleConsumer> consumers = new HashSet<>();


    InnerFragment(SerializableController c) {
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
                Field f = View.class.getDeclaredField("mParent");
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
        for (ViewLifecycleConsumer c : consumers) {
            c.onCreate(savedInstanceState);
        }
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
        for (ViewLifecycleConsumer c : consumers) {
            c.onStart();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        for (ViewLifecycleConsumer c : consumers) {
            c.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        for (ViewLifecycleConsumer c : consumers) {
            c.onPause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        for (ViewLifecycleConsumer c : consumers) {
            c.onStop();
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override public void onDestroyView() {
        super.onDestroyView();

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
        if (binding != null) {
            binding = null;
        }
        for (ViewLifecycleConsumer c : consumers) {
            c.onDestroy();
            unsubscribe(c);
        }
    }

    void subscribe(ViewLifecycleConsumer consumer) {
        consumers.add(consumer);
    }

    void unsubscribe(ViewLifecycleConsumer consumer) {
        consumers.remove(consumer);
    }
}