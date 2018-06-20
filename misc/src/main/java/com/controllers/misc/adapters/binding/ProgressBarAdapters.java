package com.controllers.misc.adapters.binding;

import android.databinding.BindingAdapter;
import android.graphics.PorterDuff;
import android.support.annotation.ColorInt;
import android.widget.ProgressBar;

/**
 * Created by Artem Sisetskyi on 6/20/18.
 * AppDevelopmentShop
 * sisetskyi.a@gmail.com
 */
public abstract class ProgressBarAdapters {

    @BindingAdapter("android:progressBackgroundTint")
    public static void _bindProgressTint(ProgressBar progressBar, @ColorInt int color) {
        progressBar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }
}
