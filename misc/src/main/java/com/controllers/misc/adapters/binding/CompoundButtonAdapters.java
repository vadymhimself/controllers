package com.controllers.misc.adapters.binding;

import android.databinding.BindingAdapter;
import android.widget.CompoundButton;

/**
 * Created by Artem Sisetskyi on 6/20/18.
 * AppDevelopmentShop
 * sisetskyi.a@gmail.com
 */
public abstract class CompoundButtonAdapters {

    @BindingAdapter("android:onCheckedChanged")
    public static void _bindOnCheckedChangedListener(CompoundButton v, CompoundButton.OnCheckedChangeListener l) {
        v.setOnCheckedChangeListener(l);
    }

    @BindingAdapter("checkedChangedListener")
    public static void _bindCheckedChangedListener(CompoundButton cb,
                                                  CompoundButton.OnCheckedChangeListener listener) {
        cb.setOnCheckedChangeListener(listener);
    }
}
