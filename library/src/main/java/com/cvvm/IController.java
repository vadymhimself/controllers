package com.cvvm;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

/**
 * Created by Vadim Ovcharenko
 * 18.10.2016.
 */

interface IController {
    @NonNull String getTag();
    @NonNull Fragment asFragment();
    @LayoutRes int getLayoutId();
}
