package com.controllers.misc.adapters.binding;

import android.databinding.BindingAdapter;
import android.widget.LinearLayout;

/**
 * Created by Artem Sisetskyi on 6/20/18.
 * AppDevelopmentShop
 * sisetskyi.a@gmail.com
 */
public abstract class LinearLayoutAdapters {

    @BindingAdapter("android:orientation")
    public static void _bindOrientationLayout(LinearLayout iv, boolean isHorizontal) {
        iv.setOrientation(isHorizontal ? android.widget.LinearLayout.HORIZONTAL : android.widget.LinearLayout.VERTICAL);
    }
}
