package com.controllers.misc;

import android.databinding.BindingAdapter;
import android.view.View;

public abstract class BindingAdapters {

    @BindingAdapter("android:visibility")
    public static void bindVisibility(View view, Boolean visibility) {
        if (visibility != null) {
            view.setVisibility(visibility ? View.VISIBLE : View.GONE);
        }
    }

}
