package com.cvvm;

import retrofit2.Call;
import retrofit2.Response;

public class TaskCallAdapter<T> implements Task<T> {

    final Call<T> actual;

    public TaskCallAdapter(Call<T> actual) {
        this.actual = actual;
    }

    @Override
    public T exec() throws Throwable {
        Response<T> res = actual.execute();
        if (res.isSuccessful()) {
            return res.body();
        } else {
            throw new NetworkError(res.code(), res.message());
        }
    }

    @Override
    public void cancel() {
        actual.cancel();
    }
}
