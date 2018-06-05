package com.controllers;

import android.os.Looper;
import android.util.Log;

import java.io.Serializable;

/**
 * Created by Vadym Ovcharenko
 * 21.10.2016.
 */

class PromiseImpl<T> implements PromiseInternal<T>, Serializable,
        ObservableController.Observer {

    private static final String TAG = PromiseImpl.class.getSimpleName();

    private final Task<T> task;
    private final SuccessAction<T> successAction;
    private final ErrorAction errorAction;
    private final Action cancelledAction;
    private final Action finallyAction;

    private ObservableController context;
    private boolean terminated;
    private boolean cancelled;

    PromiseImpl(ObservableController context, Task<T> task,
                SuccessAction<T> successAction, ErrorAction errorAction,
                Action cancelledAction, Action finallyAction) {
        this.context = context;
        this.task = task;
        this.successAction = successAction;
        this.errorAction = errorAction;
        this.cancelledAction = cancelledAction;
        this.finallyAction = finallyAction;

        // track lifecycle of the host controller
        if (context != null)
            context.addObserver(this);
    }

    @Override
    public void deliverSuccess(T result) {
        checkThread();
        terminate();
        if (successAction != null) {
            successAction.call(result);
        }
    }

    @Override
    public void deliverError(Throwable err) {
        checkThread();
        terminate();

        if (errorAction == null) {
            Request.defaultErrorAction.call(err);
            return;
        }

        try {
            errorAction.call(err);
        } catch (Throwable throwable) {
            Log.e(TAG, "Exception thrown while delivering error: ", err);
            throw throwable;
        }
    }

    @Override
    public void deliverCancelled() {
        checkThread();
        terminate();
        cancelled = true;
        if (cancelledAction != null) {
            cancelledAction.call();
        }
    }

    @Override
    public void deliverFinally() {
        checkThread();
        // promise must be terminated already
        ensureTerminated();

        if (finallyAction != null) {
            finallyAction.call();
        }
        if (context != null) {
            context.removeObserver(this);
            context = null;
        }
    }

    @Override
    public boolean hasTerminated() {
        return terminated;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void enqueue() {
        TaskExecutor.getInstance().enqueue(this);
    }

    @Override
    public void cancel() {
        TaskExecutor.getInstance().cancel(this);
    }

    @Override
    public Task<T> getTask() {
        return task;
    }

    @Override
    public void onRestored(ObservableController observable) {
        if (!terminated) {
            // Self-enqueue upon restoring if was not terminated
            enqueue();
        }
    }

    @Override
    public void onDetachedFromStack(ObservableController observable) {
        // Host controller was detached from stack and will probably get
        // garbage collected. The promise will never be satisfied for it.
        if (!terminated) {
            cancel();
            // observer removed when deliverFinally called
        }
    }

    @Override
    public void onAttachedToStack(ObservableController observable) {

    }

    @Override
    public void onAttachedToScreen(ObservableController observable) {

    }

    @Override
    public void onDetachedFromScreen(ObservableController observable) {

    }

    private void checkThread() {
        if (Looper.getMainLooper().getThread() != Thread.currentThread()) {
            throw new IllegalStateException("Promise results must be " +
                    "delivered on the main thread");
        }
    }

    private void terminate() {
        ensureNotTerminated();
        terminated = true;
    }

    private void ensureNotTerminated() {
        if (terminated) {
            throw new IllegalStateException("Promise unexpectedly terminated");
        }
    }

    private void ensureTerminated() {
        if (!terminated) {
            throw new IllegalStateException();
        }
    }
}
