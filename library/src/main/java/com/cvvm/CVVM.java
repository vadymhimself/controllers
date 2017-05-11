package com.cvvm;

import android.util.Log;

/**
 * Created by Vadim Ovcharenko
 * 06.11.2016.
 */

public class CVVM {
    static Promise.ErrorAction defaultErrorAction = new Promise.ErrorAction() {
        @Override public void call(Throwable e) {
            Log.e(PromiseImpl.class.getSimpleName(), "execution error", e);
        }
    };

    public static void setDefaultErrorAction(Promise.ErrorAction errorAction) {
        defaultErrorAction = errorAction;
    }
}
