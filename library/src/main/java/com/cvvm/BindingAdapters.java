package com.cvvm;

import android.databinding.BindingAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by Vadym Ovcharenko
 * 05.11.2016.
 */

public class BindingAdapters {

    @BindingAdapter("adapter")
    public static void bindPagerAdapter(final ViewPager viewPager, final ControllerPagerAdapter adapter) {
        viewPager.setAdapter(adapter.asFragmentPagerAdapter());
    }
}
