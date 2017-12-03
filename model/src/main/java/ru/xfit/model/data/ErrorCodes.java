package ru.xfit.model.data;

import java.io.Serializable;

/**
 * Created by TESLA on 22.11.2017.
 */

public enum ErrorCodes implements Serializable {
    UNEXPECTED_SERVER_ERROR("UNEXPECTED_SERVER_ERROR"),
    USER_ID_NOT_SPECIFIED("USER_ID_NOT_SPECIFIED"),
    INVALID_USER_ID_OR_PASSWORD("INVALID_USER_ID_OR_PASSWORD"),
    BACKEND_ERROR("BACKEND_ERROR"),
    PHONE_USED("PHONE_USED"),
    SMS_REQUESTED_TOO_OFTEN("SMS_REQUESTED_TOO_OFTEN"),
    CLASS_ALREADY_EXISTS("CLASS_ALREADY_EXISTS"),
    ACCESS_DENIED("ACCESS_DENIED"),
    NOT_IMPLEMENTED("NOT_IMPLEMENTED"),
    NOT_FOUND("NOT_FOUND"),
    NO_LINKED_CLUBS("NO_LINKED_CLUBS");

    private String errorCode;

    ErrorCodes(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
