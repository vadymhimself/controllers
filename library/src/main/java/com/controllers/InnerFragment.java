package com.controllers;

import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.controllers.AbstractController.ViewLifecycleConsumer;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * Representation of the basic Controller in terms of Android
 * Created by Vadym Ovcharenko
 * 27.11.2016.
 */

// must be public
public final class InnerFragment<B extends ViewDataBinding> extends Fragment {

    private static final String KEY_FRAG = "_inner_fragment";

    AbstractController controller;
    B binding;

    private final Set<ViewLifecycleConsumer> consumers = new HashSet<>();

    static <B extends ViewDataBinding> InnerFragment<B> createInstance(SerializableController c) {
        InnerFragment<B> fragment = new InnerFragment<>();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_FRAG, c);
        fragment.setArguments(bundle);
        return fragment;
    }

    @SuppressWarnings("unchecked")
    @Nullable @Override public View onCreateView(LayoutInflater inflater,
                                                 @Nullable ViewGroup p,
                                                 @Nullable Bundle savedInstanceState) {
        // sometimes, we need fragment to save it's state (for example in ViewPager)
        controller = (AbstractController) getArguments().getSerializable(KEY_FRAG);
        if (controller == null) {
            throw new IllegalStateException();
        }

        int layoutRes = controller.getLayoutId();
        if (layoutRes == 0) {
            throw new IllegalStateException(controller.getClass().getSimpleName() + " has 0x0 layout resource ID");
        }

        binding = DataBindingUtil.inflate(inflater, layoutRes, p, false);

        if (binding == null) {
            throw new IllegalStateException(
                    "Failed to inflate layout for " +
                    controller.getClass().getSimpleName() +
                    ". Make sure to declare binding <layout> in xml."
            );
        }

        // inject controller
        binding.setVariable(BR.controller, controller);
        binding.executePendingBindings();

        for (ViewLifecycleConsumer c : consumers) {
            c.onCreate(savedInstanceState);
        }

        return binding.getRoot();
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

    @Override public void onDestroyView() {
        super.onDestroyView();

        if (binding != null) {
            binding.unbind();
            binding = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (ViewLifecycleConsumer c : consumers) {
            c.onDestroy();
            unsubscribe(c);
        }
    }

    private static final int DEFAULT_CHILD_ANIMATION_DURATION = 250;

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        final Fragment parent = getParentFragment();

        // Apply the workaround only if this is a child fragment, and the parent
        // is being removed.
        if (!enter && parent != null && parent.isRemoving()) {
            // This is a workaround for the bug where child fragments disappear when
            // the parent is removed (as all children are first removed from the parent)
            // See https://code.google.com/p/android/issues/detail?id=55228
            Animation doNothingAnim = new AlphaAnimation(1, 1);
            doNothingAnim.setDuration(getNextAnimationDuration(parent, DEFAULT_CHILD_ANIMATION_DURATION));
            return doNothingAnim;
        } else {
            return super.onCreateAnimation(transit, enter, nextAnim);
        }
    }

    private static long getNextAnimationDuration(Fragment fragment, long defValue) {
        try {
            // Attempt to get the resource ID of the next animation that
            // will be applied to the given fragment.
            Field nextAnimField = Fragment.class.getDeclaredField("mNextAnim");
            nextAnimField.setAccessible(true);
            int nextAnimResource = nextAnimField.getInt(fragment);
            Animation nextAnim = AnimationUtils.loadAnimation(fragment.getActivity(), nextAnimResource);

            // ...and if it can be loaded, return that animation's duration
            return (nextAnim == null) ? defValue : nextAnim.getDuration();
        } catch (NoSuchFieldException|IllegalAccessException|Resources.NotFoundException ex) {
            Log.w(InnerFragment.class.getSimpleName(), "Unable to load next animation from parent.", ex);
            return defValue;
        }
    }

    void subscribe(ViewLifecycleConsumer consumer) {
        consumers.add(consumer);
    }

    void unsubscribe(ViewLifecycleConsumer consumer) {
        consumers.remove(consumer);
    }
}