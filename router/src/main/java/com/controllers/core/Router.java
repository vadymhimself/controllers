package com.controllers.core;

import android.support.annotation.AnimRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.controllers.Controller;

public interface Router<T extends ViewModel> {

    boolean show(Controller controller);

    boolean show(@NonNull Controller next,
              @AnimRes int enter, @AnimRes int exit);

    boolean back();

    boolean back(@AnimRes int enter, @AnimRes int exit);

    boolean replace(Controller controller);

    boolean replace(Controller next, @AnimRes int enter, @AnimRes int exit);

    boolean goBackTo(Controller controller);

    boolean goBackTo(Controller controller, @AnimRes int enter, @AnimRes int exit);

    /**
     * Clear the stack of controllers and place one on the top
     * @param controller controller to place on top
     * @return top controller
     */
    boolean clear(Controller controller);

    boolean clear(Controller controller, @AnimRes int enter, @AnimRes int exit);

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
