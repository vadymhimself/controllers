package com.cvvm;

import android.os.Handler;
import android.os.Looper;


import java.util.concurrent.Executor;

/**
 * Created by Vadim Ovcharenko
 * 21.10.2016.
 */

class MainThreadExecutor implements Executor {

    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override public void execute(Runnable runnable) {
        handler.post(runnable);
    }
}
