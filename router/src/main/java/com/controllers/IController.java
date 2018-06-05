package com.controllers;

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

    @NonNull Object getTag();
    @LayoutRes int getLayoutId();
}
