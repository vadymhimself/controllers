package com.cvvm;

import android.databinding.ViewDataBinding;
import android.support.v4.app.Fragment;

/**
 * Created by Vadym Ovcharenko
 * 07.11.2016.
 */

class FragmentStrategy<B extends ViewDataBinding> implements AbstractController.Strategy<B> {

    private transient final InnerFragment<B> fragment;

    FragmentStrategy(SerializableController controller) {
        fragment = InnerFragment.createInstance(controller);
        fragment.setRetainInstance(false);
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

}
