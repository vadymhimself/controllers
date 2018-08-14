package com.controllers.core;

import android.support.annotation.Nullable;
import com.controllers.Controller;

public interface Router<T extends ViewModel> {

    @Nullable
    <C> C findByClass(Class<C> clazz);

    @Nullable
    Controller findByTag(Object tag);

    @Nullable
    Controller getPrevious();

    @Nullable
    Controller getTop();

    @Nullable
    Controller getBottom();

    boolean make(Transition<T> transition);

    interface Transition<R extends ViewModel> {
        boolean run(RouterStack<R> stack);
    }
}
