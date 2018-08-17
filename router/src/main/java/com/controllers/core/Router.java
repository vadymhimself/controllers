package com.controllers.core;

import android.support.annotation.Nullable;

public interface Router<T extends ViewModel> extends Iterable<T> {

    @Nullable
    <C> C findByClass(Class<C> clazz);

    @Nullable
    T getPrevious();

    @Nullable
    T getTop();

    @Nullable
    T getBottom();

    int size();

    boolean make(Transition<T> transition);

    interface Transition<R extends ViewModel> {
        boolean run(RouterStack<R> stack);
    }
}
