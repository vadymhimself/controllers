package com.controllers.misc;

import android.os.Handler;

import java.util.HashMap;
import java.util.Map;

public class Debouncer {

    private final Handler handler;
    private final Map<Object, Runnable> tags = new HashMap<>();

    public Debouncer(Handler handler) {
        this.handler = handler;
    }

    public void bounce(Object tag, long delay, Runnable runnable) {
        tags.put(tag, runnable);
        handler.postDelayed(() -> {
            if (tags.get(tag) == runnable) {
                runnable.run();
                tags.remove(tag);
            }
        }, delay);
    }

    public void cancel(Object tag) {
        tags.remove(tag);
    }
}
