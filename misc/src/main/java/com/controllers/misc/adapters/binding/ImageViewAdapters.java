package com.controllers.misc.adapters.binding;

import android.content.res.ColorStateList;
import android.databinding.BindingAdapter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.ImageViewCompat;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

/**
 * Created by Artem Sisetskyi on 6/20/18.
 * AppDevelopmentShop
 * sisetskyi.a@gmail.com
 */
public abstract class ImageViewAdapters {

    @BindingAdapter("android:src")
    public static void _bindSrc(ImageView iv, @DrawableRes Integer res) {
        if (res != null) {
            iv.setImageResource(res);
        } else {
            iv.setImageDrawable(null);
        }
    }

    @BindingAdapter("android:scaleType")
    public static void _bindScaleImageView(ImageView view, ImageView.ScaleType type) {
        view.setScaleType(type);
    }

  @BindingAdapter(value = {
      "android:src", "placeholder", "fallback", "circleTransform"
  }, requireAll = false)
  public static void _bindSrc(ImageView iv, @Nullable Object image,
      @Nullable Object placeholder, @Nullable Object fallBack, boolean circleTransform) {
        RequestOptions options = new RequestOptions();

    if (placeholder != null) {
      if (placeholder instanceof Drawable) {
        options = options.placeholder((Drawable) placeholder);
      } else if (placeholder instanceof Integer) {
        options = options.placeholder((Integer) placeholder);
      } else {
        throw new UnsupportedOperationException(
            "Unsupported placeholder type " + placeholder.getClass());
      }
    }
    if (fallBack != null) {
      if (fallBack instanceof Drawable) {
        options = options.fallback((Drawable) fallBack);
      } else if (fallBack instanceof Integer) {
        options = options.fallback((Integer) fallBack);
      } else {
        throw new UnsupportedOperationException("Unsupported fallBack type " + fallBack.getClass());
      }
    }
        if (circleTransform) {
            options = options.circleCrop();
        }
    options = options.dontAnimate();
    Glide.with(iv).load(image).apply(options).into(iv);
    }

    @BindingAdapter("circleSrc")
    public static void _bindCircleSrc(ImageView iv, @DrawableRes Integer src) {
        Glide.with(iv)
                .load(src)
                .apply(new RequestOptions()
                        .circleCrop())
                .into(iv);
    }

    @BindingAdapter("srcCompat")
    public static void _bindSrcCompat(ImageView iv, @DrawableRes int res) {
        iv.setImageResource(res);
    }

    @BindingAdapter("backgroundTint")
    public static void _bindBgrTint(ImageView view, @ColorRes int colorRes) {
        view.getBackground().mutate()
            .setColorFilter(ContextCompat.getColor(view.getContext(), colorRes),
                PorterDuff.Mode.MULTIPLY);
    }

    @BindingAdapter("tintColor")
    public static void _bindTintColor(ImageView iv, @ColorInt int colorInt) {
        ImageViewCompat.setImageTintList(iv, ColorStateList.valueOf(colorInt));
    }

    @BindingAdapter("tint")
    public static void _bindTint(ImageView iv, @ColorRes int res) {
        Drawable d = DrawableCompat.wrap(iv.getDrawable());
        DrawableCompat.setTint(d, ContextCompat.getColor(iv.getContext(), res));
        iv.setImageDrawable(d);
    }

    @BindingAdapter("tintList")
    public static void _bindTintList(ImageView iv, @ColorRes int colorInt) {
        ImageViewCompat.setImageTintList(iv, iv.getContext().getResources().getColorStateList(colorInt));
    }

}
