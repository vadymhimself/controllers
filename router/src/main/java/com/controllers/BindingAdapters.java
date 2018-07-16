package com.controllers;

import android.databinding.BindingAdapter;
import android.util.Log;
import android.view.View;
import com.controllers.AbstractController.ViewLifecycleConsumer;

/**
 * Created by Vadym Ovcharenko
 * 05.11.2016.
 */

public class BindingAdapters {

    @BindingAdapter("lifecycle")
    public static void bindLifeCycle(final View view,
                                     Controller controller) {
        if (Const.LOGV)
            Log.w(Const.LOG_PREFIX, "using default binding adapter");

        if (view instanceof ViewLifecycleConsumer)
            controller.subscribe((ViewLifecycleConsumer) view);
        else
            throw new IllegalArgumentException();
    }
}
