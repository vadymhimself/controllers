package com.cvvm;

import android.os.AsyncTask;
import android.os.Looper;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * Created by Vadim Ovcharenko
 * 21.10.2016.
 */

final class TaskExecutor {

    private static TaskExecutor instance;

    static synchronized TaskExecutor getInstance() {
        if (instance == null) {
            Executor worker = AsyncTask.THREAD_POOL_EXECUTOR;
            Executor mainThreadExecutor = new MainThreadExecutor();
            instance = new TaskExecutor(worker, mainThreadExecutor);
        }
        return instance;
    }

    private final Executor workerThreadExecutor;
    private final Executor mainThreadExecutor;

    private final Map<Integer, PromiseInternal> queue = Collections.synchronizedMap(new LinkedHashMap<Integer, PromiseInternal>());

    private TaskExecutor(Executor workerThreadExecutor, Executor mainThreadExecutor) {
        this.workerThreadExecutor = workerThreadExecutor;
        this.mainThreadExecutor = mainThreadExecutor;
    }

    @SuppressWarnings("unchecked")
    <T> void enqueue(PromiseInternal<T> promise) {
        checkState();
        if (promise == null || promise.hasTerminated()) throw new IllegalArgumentException();

        final Task<T> task = promise.getTask();
        final int taskHash = task.hashCode();

        if (queue.put(taskHash, promise) != null) {
            // means that execution is already running
            return;
        }

        workerThreadExecutor.execute(new Runnable() {
            @Override public void run() {
                Throwable throwable = null;
                PromiseInternal<T> actual = null;

                try {
                    final T result = task.exec();

                    final PromiseInternal<T> p = (PromiseInternal<T>) queue.get(taskHash);
                    if (p == null) return; // may have been cancelled by this time

                    mainThreadExecutor.execute(new Runnable() {
                        @Override public void run() {
                            if (!p.isCancelled()) p.deliverSuccess(result); // finally called later
                        }
                    });

                    actual = p;
                } catch (final Throwable t) {
                    throwable = t;
                }

                final Throwable t = throwable;
                final PromiseInternal<T> p;

                if (actual != null) {
                    // may be null if error thrown
                    p = actual;
                } else {
                    p = (PromiseInternal) queue.get(taskHash);
                }

                queue.remove(taskHash);

                if (p.isCancelled()) return;

                mainThreadExecutor.execute(new Runnable() {
                    @Override public void run() {
                        if (t != null) {
                            p.deliverError(t);
                        }
                        p.deliverFinally();
                    }
                });
            }
        });
    }

    void cancel(PromiseInternal promise) {
        checkState();

        int requestHash = promise.getTask().hashCode();
        boolean localMustCancel;
        final PromiseInternal actual;

        synchronized (queue) {
            actual = queue.get(requestHash);
            localMustCancel = actual == promise && !actual.hasTerminated();
        }

        if (localMustCancel) {
            actual.getTask().cancel();
            actual.deliverCancelled();
            actual.deliverFinally();
        }
    }

    private void checkState() {
        if (Looper.getMainLooper().getThread() != Thread.currentThread()) {
            throw new IllegalStateException("Executor called outside of main thread");
        }
    }
}
