package com.controllers;

/**
 * + * Created by Vadym Ovcharenko
 * + * 21.10.2016.
 * +
 */

class RequestFactoryImpl<I> implements RequestFactory<I> {

    private final Controller context;
    private final I service;

    RequestFactoryImpl(Controller context, Class<I> service) {
        this.context = context;
        this.service = ServiceContainer.getService(service);
        if (this.service == null) {
            throw new IllegalStateException("Service is not registered. " +
                    "Register through ServiceContainer#registerService first");
        }
    }

    @Override
    public <T> Request<T> create(RequestFactory.RequestFunction<Task<T>, I> callFunc) {
        return new RequestImpl<>(context, callFunc.call(service));
    }
}