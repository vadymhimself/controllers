package com.controllers.misc.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

public final class UiUtils {
    public static final int BASELINE_WIDTH_DP = 360;
    public static final int BASELINE_HEIGHT_DP = 640;
    public static final float HEADER_RATIO = 0.317f;

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public static int getHeaderHeight() {
        return (int) (getScreenHeight() * HEADER_RATIO);
    }

    public static int dpToPx(int dp, Context context) {
        Resources r = context.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    public static int spToPx(float sp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    public static float getWidthScaleFactor(Context context) {
        return getScreenWidth() / (float) dpToPx(BASELINE_WIDTH_DP, context);
    }

    public static float getHeightScaleFactor(Context context) {
        return getScreenHeight() / (float) dpToPx(BASELINE_HEIGHT_DP, context);
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
