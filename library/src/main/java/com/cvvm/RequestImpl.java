package com.cvvm;

import android.support.annotation.NonNull;

/**
 * Basically a {@link Promise} builder
 * Created by Vadim Ovcharenko
 * 21.10.2016.
 */

class RequestImpl<T> implements Request<T> {

    private ExecutionContext context;
    private Promise.ErrorAction errorAction;
    private Promise.Action cancelledAction;
    private Promise.Action finallyAction;

    private final Task<T> task;

    RequestImpl(@NonNull ExecutionContext context, Task<T> task) {
        this.context = context;
        this.task = task;
    }

    @Override public Request<T> onError(Promise.ErrorAction errorAction) {
        this.errorAction = errorAction;
        return this;
    }

    @Override public Request<T> onCanceled(Promise.Action cancelledAction) {
        this.cancelledAction = cancelledAction;
        return this;
    }

    @Override public Request<T> onFinally(Promise.Action finallyAction) {
        this.finallyAction = finallyAction;
        return this;
    }

    @Override public final Promise<T> execute(@NonNull Promise.SuccessAction<T> successAction) {
        PromiseImpl<T> promise = new PromiseImpl<>(
                context,
                task,
                successAction,
                errorAction,
                cancelledAction,
                finallyAction
        );

        promise.enqueue();

        return promise;
    }

    @Override
    public Promise<T> execute() {
        return execute(null);
    }
}
