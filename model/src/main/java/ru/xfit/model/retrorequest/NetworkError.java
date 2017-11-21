package ru.xfit.model.retrorequest;

import java.io.IOException;

import ru.xfit.model.data.ErrorResponse;

/**
 * Created by Vadym Ovcharenko
 * 05.11.2016.
 */

public class NetworkError extends IOException {
    final int code;
    final String message;
    final ErrorResponse errorResponse;

    public NetworkError(int code, String message, ErrorResponse errorResponse) {
        super(code + " " + message);
        this.code = code;
        this.message = message;
        this.errorResponse = errorResponse;
    }

    public int getCode() {
        return code;
    }

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }

    public String getMessage() {
        return message;
    }

    @Override public String toString() {
        return "NetworkError{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
