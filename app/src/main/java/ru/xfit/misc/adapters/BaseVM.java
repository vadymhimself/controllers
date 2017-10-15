package ru.xfit.misc.adapters;

import android.support.annotation.LayoutRes;

import java.io.Serializable;

public interface BaseVM extends Serializable {
    @LayoutRes int getLayoutId();
}
