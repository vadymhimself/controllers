package com.controllers.misc.adapters.binding;

import android.databinding.BindingAdapter;
import android.support.v4.view.ViewPager;

/**
 * Created by Artem Sisetskyi on 6/20/18.
 * AppDevelopmentShop
 * sisetskyi.a@gmail.com
 */
public abstract class ViewPagerAdapters {

    @BindingAdapter("pageChangeListener")
    public static void _bindPageChangeListener(ViewPager pager, ViewPager.OnPageChangeListener pageChangeListener) {
        pager.addOnPageChangeListener(pageChangeListener);
    }


}
