package com.cvvm;

import android.databinding.BaseObservable;
import android.databinding.ViewDataBinding;
import android.support.annotation.Nullable;

/**
 * Created by Vadim Ovcharenko
 * 01.11.2016.
 */

abstract class AbstractViewModel<C extends AbstractController, B extends ViewDataBinding> extends BaseObservable implements IViewModel<C> {

    private transient C controller;
    private transient B binding;

    // must be public
    public AbstractViewModel() {
    }

    @Nullable @Override public final C getController() {
        return controller;
    }

    @Nullable protected final B getBinding() {
        return binding;
    }

    void onAttach(C controller, B binding) {
        this.controller = controller;
        this.binding = binding;
    }

    void onDetach() {
        this.controller = null;
        this.binding = null;
    };
}
