package com.controllers.misc.utils;

import android.os.Handler;
import android.os.Looper;


public final class AsyncUtils {

    private static Handler handler = new Handler(Looper.getMainLooper());

    public static void postDelayed(long millis, Runnable runnable) {
        handler.postDelayed(runnable, millis);
    }
}
