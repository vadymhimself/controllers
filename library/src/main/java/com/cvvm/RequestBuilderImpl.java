package com.cvvm;

import retrofit2.Call;

/**
 * Created by Vadim Ovcharenko
 * 21.10.2016.
 */

class RequestBuilderImpl<I> implements RequestBuilder<I> {

    private final ExecutionContext context;
    private final I service;

    RequestBuilderImpl(ExecutionContext context, Class<I> service) {
        this.context = context;
        this.service = ServiceContainer.getService(service);
        if (this.service == null) {
            throw new IllegalStateException("Service is not registered. " +
                    "Register through ServiceContainer#registerService first");
        }
    }

    @Override public <T> Request<T> task(RequestFunction<Task<T>, I> callFunc) {
        return new RequestImpl<>(context,  callFunc.call(service));
    }
}
