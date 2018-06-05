package com.controllers;


/**
 * Created by Vadym Ovcharenko
 * 27.10.2016.
 */

interface PromiseInternal<T> extends Promise<T> {

    /**
     * This method is hosted in package local interface, because the user
     * should not be able to manually execute promises.
     */
    void enqueue();

    Task<T> getTask();

    void deliverSuccess(T result);

    void deliverError(Throwable err);

    void deliverFinally();

    void deliverCancelled();
}
