package com.controllers.misc.adapters.binding;

import android.databinding.BindingAdapter;
import android.support.design.widget.BottomNavigationView;

/**
 * Created by Artem Sisetskyi on 6/20/18.
 * AppDevelopmentShop
 * sisetskyi.a@gmail.com
 */
public abstract class BottomNavigationViewAdapters {

    @BindingAdapter("itemSelectedListener")
    public static void _bindItemSelected(BottomNavigationView view,
                                        BottomNavigationView.OnNavigationItemSelectedListener l) {
        view.setOnNavigationItemSelectedListener(l);
    }

    @BindingAdapter("menu")
    public static void _bindMenu(BottomNavigationView bottomNavigationView, Integer menuRes) {
        bottomNavigationView.getMenu().clear();
        bottomNavigationView.inflateMenu(menuRes);
    }
}
