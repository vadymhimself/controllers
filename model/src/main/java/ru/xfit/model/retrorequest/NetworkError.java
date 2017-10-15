package ru.xfit.model.retrorequest;

import java.io.IOException;

/**
 * Created by Vadym Ovcharenko
 * 05.11.2016.
 */

public class NetworkError extends IOException {
    final int code;
    final String message;

    public NetworkError(int code, String message) {
        super(code + " " + message);
        this.code = code;
        this.message = message;
    }

    @Override public String toString() {
        return "NetworkError{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
