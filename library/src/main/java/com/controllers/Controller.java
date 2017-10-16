package com.controllers;

import android.databinding.ViewDataBinding;

/**
 * Fragment-based controller
 * Created by Vadym Ovcharenko
 * 20.10.2016.
 */

public abstract class Controller<B extends ViewDataBinding> extends
        ObservableController<B> {
    @Override
    ViewStrategy<B> createStrategy() {
        return new FragmentViewStrategy<>(this);
    }
}
