package com.cvvm;

import android.databinding.ViewDataBinding;

/**
 * Fragment-based controller
 * Created by Vadim Ovcharenko
 * 20.10.2016.
 */

public abstract class Controller<B extends ViewDataBinding> extends
        ObservableController<B> {
    @Override
    Strategy<B> createStrategy() {
        return new FragmentStrategy<>(this);
    }
}
