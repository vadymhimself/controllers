package com.cvvm;

import android.os.Looper;
import android.util.Log;

import java.io.Serializable;

/**
 * Created by Vadim Ovcharenko
 * 21.10.2016.
 */

class PromiseImpl<T> implements PromiseInternal<T>, Serializable {

    public static final String TAG = PromiseImpl.class.getSimpleName();

    private final Task<T> task;
    private final SuccessAction<T> successAction;
    private final ErrorAction errorAction;
    private final Action cancelledAction;
    private final Action finallyAction;

    private ExecutionContext context;
    private boolean terminated;
    private boolean cancelled;

    PromiseImpl(ExecutionContext context, Task<T> task, SuccessAction<T> successAction,
                ErrorAction errorAction, Action cancelledAction, Action finallyAction) {
        this.context = context;
        this.task = task;
        this.successAction = successAction;
        this.errorAction = errorAction;
        this.cancelledAction = cancelledAction;
        this.finallyAction = finallyAction;
    }

    @Override public void deliverSuccess(T result) {
        checkState();
        if (successAction != null) {
            successAction.call(result);
        }
    }

    @Override public void deliverError(Throwable err) {
        checkState();
        if (errorAction != null) {
            try {
                errorAction.call(err);
            } catch (Throwable throwable) {
                Log.e(TAG, "Exception thrown while delivering error: ", err);
                throw throwable;
            }
        } else {
            CVVM.defaultErrorAction.call(err);
        }
    }

    @Override public void deliverFinally() {
        checkState();
        terminated = true;
        if (finallyAction != null) {
            finallyAction.call();
        }
        context.detachPromise(this);
        context = null;
    }

    @Override public void deliverCancelled() {
        checkState();
        cancelled = true;
        if (cancelledAction != null) {
            cancelledAction.call();
        }
    }

    @Override public boolean hasTerminated() {
        return terminated;
    }

    @Override public boolean isCancelled() {
        return cancelled;
    }

    @Override public boolean isTerminated() {
        return terminated;
    }

    @Override public void enqueue() {
        context.attachPromise(this);
        TaskExecutor.getInstance().enqueue(this);
    }

    @Override public void cancel() {
        TaskExecutor.getInstance().cancel(this);
    }

    @Override public Task<T> getTask() {
        return task;
    }

    private void checkState() {
        if (terminated) {
            throw new IllegalStateException("Promise already terminated");
        }
        if (Looper.getMainLooper().getThread() != Thread.currentThread()) {
            throw new IllegalStateException("Promise results must be delivered on the main thread");
        }
    }
}
