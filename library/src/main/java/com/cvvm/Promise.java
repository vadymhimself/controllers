package com.cvvm;

import java.io.Serializable;

/**
 * Created by Vadim Ovcharenko
 * 21.10.2016.
 */

public interface Promise<T> {
    interface SuccessAction<T> extends Serializable {
        void call(T t);
    }

    interface ErrorAction extends Serializable {
        void call(Throwable e);
    }

    interface Action extends Serializable {
        void call();
    }

    void cancel();

    boolean isCancelled();

    boolean isTerminated();
}
