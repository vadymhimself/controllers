package com.cvvm;

import android.databinding.ViewDataBinding;
import android.support.v4.app.Fragment;

/**
 * Created by Vadim Ovcharenko
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
    public void subscribe(AbstractController.FragmentObserver observer) {
        fragment.subscribe(observer);
    }

    @Override
    public B getBinding() {
        return fragment.binding;
    }
//
//    @SuppressWarnings("unchecked")
//    @Override public void setViewModel(int variableId, Object viewModel) {
//        if (fragment.binding != null) {
//            fragment.binding.setVariable(variableId, viewModel);
//            if (viewModel instanceof AbstractViewModel) {
//                AbstractViewModel vm = (AbstractViewModel) viewModel;
//                vm.onAttach(fragment.controller, fragment.binding);
//            }
//        }
//    }

}
