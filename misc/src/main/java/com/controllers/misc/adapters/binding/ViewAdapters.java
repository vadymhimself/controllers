package com.controllers.misc.adapters.binding;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by Artem Sisetskyi on 6/20/18.
 * AppDevelopmentShop
 * sisetskyi.a@gmail.com
 */
public abstract class ViewAdapters {

    @BindingAdapter("android:visibility")
    public static void _bindVisibility(View view, Boolean visibility) {
        if (visibility != null) {
            view.setVisibility(visibility ? View.VISIBLE : View.GONE);
        }
    }

    @BindingAdapter("android:visibility")
    public static void _bindVisibility(View view, int visibility) {
        view.setVisibility(visibility);
    }

    @BindingAdapter("android:enabled")
    public static void _bindEnabled(View view, Boolean enabled) {
        view.setEnabled(enabled);
    }

    @BindingAdapter("android:background")
    public static void _bindBackground(View view, @DrawableRes Integer res) {
        if (res != null) {
            view.setBackground(ContextCompat.getDrawable(view.getContext(), res));
        }
    }

    @BindingAdapter("android:foreground")
    public static void _bindForeground(View view, @DrawableRes int res) {
        _bindForeground(view, ContextCompat.getDrawable(view.getContext(), res));
    }

    @BindingAdapter("android:foreground")
    public static void _bindForeground(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (view instanceof FrameLayout) {
                FrameLayout fl = (FrameLayout) view;
                fl.setForeground(drawable);
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    view.setForeground(drawable);
                }
            }
        }
    }

    @BindingAdapter("android:minWidth")
    public static void _bindMinWidth(View view, int width) {
        view.setMinimumWidth(width);
    }

    @BindingAdapter("focusChangeListener")
    public static void _bindFocusChangeListener(View view, View.OnFocusChangeListener listener) {
        view.setOnFocusChangeListener(listener);
    }

    @BindingAdapter("android:layout_marginLeft")
    public static void _bindMarginLeft(View v, float margin) {
        ((ViewGroup.MarginLayoutParams) v.getLayoutParams()).leftMargin = (int) margin;
    }

    @BindingAdapter("android:layout_weight")
    public static void _bindLayoutWeight(View v, int weight) {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) v.getLayoutParams();
        lp.weight = weight;
    }

    @BindingAdapter("android:layout_marginRight")
    public static void _bindMarginRight(View v, float margin) {
        ((ViewGroup.MarginLayoutParams) v.getLayoutParams()).rightMargin = (int) margin;
    }

    @BindingAdapter("android:layout_marginBottom")
    public static void _bindMarginBottom(View v, int marginTop) {
        ((ViewGroup.MarginLayoutParams) v.getLayoutParams()).bottomMargin = marginTop;
    }

    @BindingAdapter("android:minHeight")
    public static void _bindLayoutHeight(View v, int height) {
        v.setMinimumHeight(height);
    }

    @BindingAdapter("android:layout_marginTop")
    public static void _bindMarginTop(View v, int marginTop) {
        ((ViewGroup.MarginLayoutParams) v.getLayoutParams()).topMargin = marginTop;
    }

    @BindingAdapter("android:paddingTop")
    public static void _bindPaddingTop(View v, int top) {
        v.setPadding(v.getPaddingLeft(), top, v.getPaddingRight(), v.getPaddingBottom());
    }

    @BindingAdapter("android:paddingBottom")
    public static void _bindPaddingBottom(View v, int bottom) {
        v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), bottom);
    }

    @BindingAdapter("android:paddingLeft")
    public static void _bindPaddingLeft(View v, int left) {
        v.setPadding(left, v.getPaddingTop(), v.getPaddingRight(), v.getPaddingBottom());
    }

    @BindingAdapter("android:paddingRight")
    public static void _bindPaddingRight(View v, int right) {
        v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), right, v.getPaddingBottom());
    }

    @BindingAdapter("android:padding")
    public static void _bindPadding(View v, int padding) {
        v.setPadding(padding, padding, padding, padding);
    }

    @BindingAdapter("android:scaleX")
    public static void _bindScaleX(View v, float scale) {
        v.setScaleX(scale);
    }

    @BindingAdapter("android:scaleY")
    public static void _bindScaleY(View v, float scale) {
        v.setScaleY(scale);
    }

    @BindingAdapter("onViewReady")
    public static void _bindViewReady(View v, OnViewReadyListener l) {
        v.post(() -> l.onReady(v));
    }

  @BindingAdapter("activated")
  public static void _bindActivated(View v, boolean activated) {
    v.setActivated(activated);
  }

    public interface OnViewReadyListener {
        void onReady(View view);
    }


}
