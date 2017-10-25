package ru.xfit.model.data;

import java.io.IOException;

/**
 * Created by TESLA on 25.10.2017.
 */

public class ErrorException  extends IOException {
    final String message;

    public ErrorException(String message) {
        super(message);
        this.message = message;
    }

    public String toString() {
        return "Error{ message=\'" + this.message + '\'' + '}';
    }
}
