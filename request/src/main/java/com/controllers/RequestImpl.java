package com.controllers;

/**
 * Basically a {@link Promise} builder
 * Created by Vadym Ovcharenko
 * 21.10.2016.
 */

final class RequestImpl<T> extends Request<T> {

    private Controller context;
    private Promise.ErrorAction errorAction;
    private Promise.Action cancelledAction;
    private Promise.Action finallyAction;

    private final Task<T> task;

    RequestImpl(Controller context, Task<T> task) {
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

    @Override public final Promise<T> execute(Promise.SuccessAction<T> successAction) {
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

    @Override
    public T executeSync() throws Throwable {
        return task.exec();
    }
}
