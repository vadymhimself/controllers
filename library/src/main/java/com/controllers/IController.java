package com.controllers;

import android.support.annotation.AnimRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Controller interface for testing purposes.
 * Created by Vadym Ovcharenko
 * 18.10.2016.
 */

interface IController {

    @Nullable
    ControllerActivity getActivity();

    @NonNull
    Fragment asFragment();


    void show(Controller controller);
    void show(@NonNull Controller next,
              @AnimRes int enter, @AnimRes int exit);

    void back();
    void back(@AnimRes int enter, @AnimRes int exit);

    void replace(Controller controller);

    void goBackTo(Controller controller);
    void goBackTo(Controller controller, @AnimRes int
            enter, @AnimRes int exit);

    @Nullable
    <T extends Controller> T findByClass(Class<T> clazz);

    @Nullable
    Controller findByTag(String tag);

    @Nullable
    Controller getPrevious();

    @NonNull String getTag();
    @LayoutRes int getLayoutId();
}
