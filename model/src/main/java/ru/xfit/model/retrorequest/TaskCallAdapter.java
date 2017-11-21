package ru.xfit.model.retrorequest;

import com.controllers.Task;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Response;
import ru.xfit.model.data.ErrorResponse;

class TaskCallAdapter<T> implements Task<T> {

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
            String json = res.errorBody().string();
            ErrorResponse errorResponse = new Gson().fromJson(json, ErrorResponse.class);
            throw new NetworkError(res.code(), res.message(), errorResponse);
        }
    }

    @Override
    public void cancel() {
        actual.cancel();
    }
}
