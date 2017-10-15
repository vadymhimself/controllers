package ru.xfit.model.retrorequest;

import com.controllers.Task;
import retrofit2.Call;

public final class TaskBuilder {
    public interface AsyncFunc<T> {
        T call() throws Throwable;
    }

    public static <T> Task<T> from(Call<T> call) {
        return new TaskCallAdapter<>(call);
    }

    public static <T> Task<T> fromAsync(AsyncFunc<T> func) {
        return new Task<T>() {
            @Override
            public T exec() throws Throwable {
                return func.call();
            }

            @Override
            public void cancel() {
                // not cancellable
            }
        };
    }
}
