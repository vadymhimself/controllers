package com.controllers;

public interface RequestFactory<S> {

    <T> Request<T> create(RequestFunction<Task<T>, S> callFunc);

    interface RequestFunction<T, S> {
        T call(S s);
    }

}
