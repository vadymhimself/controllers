package com.controllers;

import android.databinding.BindingAdapter;
import android.util.Log;
import android.view.View;
import com.controllers.Controller.ViewLifecycleConsumer;

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

        Controller.BindingView bindingView = controller.getView();

        if (bindingView != null && view instanceof ViewLifecycleConsumer) {
            bindingView.subscribe((ViewLifecycleConsumer) view);
        } else {
            throw new IllegalArgumentException();
        }
    }
}
