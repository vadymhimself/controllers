package com.controllers;

import android.os.AsyncTask;
import android.os.Looper;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * Created by Vadym Ovcharenko
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

    // main thread only
    private final Map<Integer, PromiseInternal> queue = new LinkedHashMap<>();

    private TaskExecutor(Executor workerThreadExecutor, Executor
            mainThreadExecutor) {
        this.workerThreadExecutor = workerThreadExecutor;
        this.mainThreadExecutor = mainThreadExecutor;
    }

    @SuppressWarnings("unchecked")
    <T> void enqueue(PromiseInternal<T> promise) {
        checkState();

        if (promise == null || promise.hasTerminated())
            throw new IllegalArgumentException();

        final Task<T> task = promise.getTask();
        final int taskHash = task.hashCode();

        if (queue.put(taskHash, promise) != null) {
            // Means that execution is already running. This may happen if
            // the original context was lost and new deserialized context object
            // enqueues a task that is already running.
            return;
        }

        workerThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Throwable throwable = null;

                try {
                    final T result = task.exec();

                    mainThreadExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            final PromiseInternal<T> p = (PromiseInternal<T>)
                                    queue.get(taskHash);

                            if (p == null || p.isCancelled())
                                return; // may have been cancelled by this time

                            p.deliverSuccess(result);
                            // finally called later
                        }
                    });

                } catch (final Throwable t) {
                    throwable = t;
                }

                final Throwable t = throwable;

                mainThreadExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        PromiseInternal<T> p = queue.remove(taskHash);

                        // check if was cancelled during unsuccessful execution
                        if (p == null || p.isCancelled())
                            return;

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
        PromiseInternal actual = queue.remove(requestHash);

        if (actual == promise && !actual.hasTerminated()) {
            actual.getTask().cancel();
            actual.deliverCancelled();
            actual.deliverFinally();
        }
    }

    private void checkState() {
        if (Looper.getMainLooper().getThread() != Thread.currentThread()) {
            throw new IllegalStateException("Executor called outside of main " +
                    "thread");
        }
    }
}
