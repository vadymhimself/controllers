package com.controllers.misc.adapters.binding;

import android.databinding.BindingAdapter;
import android.support.annotation.MenuRes;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Created by Artem Sisetskyi on 6/20/18.
 * AppDevelopmentShop
 * sisetskyi.a@gmail.com
 */
public abstract class ToolbarAdapters {

    @BindingAdapter("navigationListener")
    public static void _bindNavigationListener(Toolbar toolbar, View.OnClickListener navListener) {
        toolbar.setNavigationOnClickListener(navListener);
    }

    @BindingAdapter("menu")
    public static void _bindMenu(Toolbar toolbar, @MenuRes Integer menuRes) {
        if (toolbar.getMenu() != null) {
            toolbar.getMenu().clear();
        }
        if (menuRes != null && menuRes != 0) {
            toolbar.inflateMenu(menuRes);
        }
    }

    @BindingAdapter("itemClickListener")
    public static void _bindItemClickListener(Toolbar toolbar, Toolbar.OnMenuItemClickListener listener) {
        toolbar.setOnMenuItemClickListener(listener);
    }
}
