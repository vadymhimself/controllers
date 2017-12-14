package com.controllers;

import android.databinding.BindingAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import com.controllers.AbstractController.ViewLifecycleConsumer;

/**
 * Created by Vadym Ovcharenko
 * 05.11.2016.
 */

public class BindingAdapters {

    @BindingAdapter("adapter")
    public static void bindPagerAdapter(final ViewPager viewPager, final ControllerPagerAdapter adapter) {
        if (Const.LOGV)
            Log.w(Const.TAG, "using default binding adapter");

        viewPager.setAdapter(adapter.asFragmentPagerAdapter());
    }

    @BindingAdapter("lifecycle")
    public static void bindLifeCycle(final View view,
                                     Controller controller) {
        if (Const.LOGV)
            Log.w(Const.TAG, "using default binding adapter");

        if (view instanceof ViewLifecycleConsumer)
            controller.subscribe((ViewLifecycleConsumer) view);
        else
            throw new IllegalArgumentException();
    }
}
