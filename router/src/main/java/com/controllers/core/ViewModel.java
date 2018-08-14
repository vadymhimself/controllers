package com.controllers.core;

import android.support.annotation.Nullable;

/**
 * Controller interface for testing purposes.
 * Created by Vadym Ovcharenko
 * 18.10.2016.
 */

public interface ViewModel<T extends View> {
    void onAttachedToRouter();
    void onDetachedFromRouter();

    void onAttachedToScreen(T view);
    void onDetachedFromScreen(T view);

    @Nullable
    T getView();
}
