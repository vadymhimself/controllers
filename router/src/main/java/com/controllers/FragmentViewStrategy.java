package com.controllers;

import android.databinding.ViewDataBinding;
import android.support.v4.app.Fragment;

/**
 * Created by Vadym Ovcharenko
 * 07.11.2016.
 */

class FragmentViewStrategy<B extends ViewDataBinding> implements AbstractController.ViewStrategy<B> {

    private final InnerFragment<B> fragment;

    FragmentViewStrategy(SerializableController controller) {
        fragment = new InnerFragment<>(controller);
    }

    @Override public ControllerActivity getActivity() {
        return (ControllerActivity) fragment.getActivity();
    }

    @Override public Fragment asFragment() {
        return fragment;
    }

    @Override
    public B getBinding() {
        return fragment.binding;
    }

    @Override
    public void subscribe(AbstractController.ViewLifecycleConsumer consumer) {
        fragment.subscribe(consumer);
    }

    @Override
    public void unsubscribe(AbstractController.ViewLifecycleConsumer consumer) {
        fragment.unsubscribe(consumer);
    }
}
