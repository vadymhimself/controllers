package com.cvvm;

import retrofit2.Call;

/**
 * Created by Vadim Ovcharenko
 * 27.10.2016.
 */

interface PromiseInternal<T> extends Promise<T> {
    boolean hasTerminated();

    void enqueue();

    Task<T> getTask();

    void deliverSuccess(T result);

    void deliverError(Throwable err);

    void deliverFinally();

    void deliverCancelled();
}
