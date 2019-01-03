package com.controllers.misc.adapters.binding;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.Html;
import android.widget.TextView;

/**
 * Created by Artem Sisetskyi on 6/20/18.
 * AppDevelopmentShop
 * sisetskyi.a@gmail.com
 */
public abstract class TextViewAdapters {

    @BindingAdapter(value = {"android:drawableRight", "android:drawableTint"}, requireAll = true)
    public static void _bindDrawableTint(TextView tv, @DrawableRes int drawableRes, @ColorRes Integer colorRes) {
        Drawable d = VectorDrawableCompat.create(tv.getResources(), drawableRes, null);
        if(d != null){
            d = d.mutate();
            DrawableCompat.setTint(d, ContextCompat.getColor(tv.getContext(), colorRes));
            tv.setCompoundDrawablesWithIntrinsicBounds(null, null, d, null);
        }
    }

    @BindingAdapter(value = {"android:drawableLeft", "android:drawableTint"}, requireAll = true)
    public static void _bindDrawableLeftTint(TextView tv, @DrawableRes int drawableRes, @ColorRes Integer colorRes) {
        Drawable d = VectorDrawableCompat.create(tv.getResources(), drawableRes, null);
        if(d != null){
          d = d.mutate();
          DrawableCompat.setTint(DrawableCompat.unwrap(d), ContextCompat.getColor(tv.getContext(), colorRes));
          tv.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);
        }
    }

    @BindingAdapter("editorActionListener")
    public static void _bindEditorActionListener(TextView textView, TextView.OnEditorActionListener listener) {
        textView.setOnEditorActionListener(listener);
    }

    @BindingAdapter("html")
    public static void _bindHtml(TextView textView, String html) {
        textView.setText(Html.fromHtml(html));
    }
}
