package com.cvvm;

import android.databinding.BindingAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by Vadim Ovcharenko
 * 05.11.2016.
 */

public class BindingAdapters {

    @BindingAdapter("adapter")
    public static void bindPagerAdapter(final ViewPager viewPager, final ControllerPagerAdapter adapter) {
        viewPager.setAdapter(adapter.asFragmentPagerAdapter());
    }

    @BindingAdapter("android:visibility")
    public static void bindVisibility(final View view, boolean visible) {
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }
}
