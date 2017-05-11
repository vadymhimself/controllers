package com.cvvm;

import retrofit2.Call;

/**
 * Created by Vadim Ovcharenko
 * 21.10.2016.
 */

public interface RequestBuilder<I> {

    <T> Request<T> task(RequestFunction<Task<T>, I> callFunc);

    interface RequestFunction<T, I> {
        T call(I i);
    }
}
