package com.controllers.misc.adapters.binding;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.design.widget.NavigationView;
import android.view.LayoutInflater;

/**
 * Created by Artem Sisetskyi on 6/20/18.
 * AppDevelopmentShop
 * sisetskyi.a@gmail.com
 */
public abstract class NavigationViewAdapters {

    @BindingAdapter("headerLayout")
    public static void _bindHeaderLayout(NavigationView view, int layoutId) {
        ViewDataBinding b = DataBindingUtil.inflate(LayoutInflater.from(view.getContext()),
                layoutId, null, false);
        view.addHeaderView(b.getRoot());
    }

}
