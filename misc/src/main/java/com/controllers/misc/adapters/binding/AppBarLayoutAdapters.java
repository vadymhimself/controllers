package com.controllers.misc.adapters.binding;

import android.databinding.BindingAdapter;
import android.support.design.widget.AppBarLayout;

/**
 * Created by Artem Sisetskyi on 6/20/18.
 * AppDevelopmentShop
 * sisetskyi.a@gmail.com
 */
public class AppBarLayoutAdapters {

    @BindingAdapter("offsetChangedListener")
    public static void _bindOnOffsetChangeListener(AppBarLayout abl,
                                                  AppBarLayout.OnOffsetChangedListener listener) {
        abl.addOnOffsetChangedListener(listener);
    }
}
