package com.controllers.misc.adapters.binding;

import android.databinding.BindingAdapter;
import android.support.annotation.IdRes;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

/**
 * Created by Artem Sisetskyi on 6/20/18.
 * AppDevelopmentShop
 * sisetskyi.a@gmail.com
 */
public abstract class TabLayoutAdapters {

    @BindingAdapter("tabSelectedListener")
    public static void _bindItemSelected(TabLayout view,
                                         TabLayout.OnTabSelectedListener listener) {
        view.addOnTabSelectedListener(listener);
    }

    @BindingAdapter("viewPager")
    public static void _bindViewPager(TabLayout tabLayout, @IdRes int pagerId) {
        ViewPager viewPager = tabLayout.getRootView().findViewById(pagerId);
        if (viewPager != null) {
            tabLayout.setupWithViewPager(viewPager);
        } else {
            throw new IllegalArgumentException("Can't find ViewPager in view hierarchy");
        }
    }

    @BindingAdapter("tabClickListener")
    public static void _bindTabClickListener(TabLayout view, TabLayout.OnTabSelectedListener listener) {
        view.addOnTabSelectedListener(listener);
    }
}
