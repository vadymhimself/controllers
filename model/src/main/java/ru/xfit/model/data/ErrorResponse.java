package ru.xfit.model.data;

import java.io.Serializable;

/**
 * Created by TESLA on 25.10.2017.
 */

public class ErrorResponse implements Serializable {
    /*{
      "code": "BACKEND_ERROR",
      "message": "Указан неверный или недействительный пользователь и/или пароль",
      "httpCode": 503,
      "cause": {
        "ErrorCode": "INVALID_USER_ID_OR_PASSWORD",
        "Temporary": "False",
        "CaseID": "ffd00d65-1b2c-4cfa-809e-740ed23b3805",
        "ClientMessage": "Указан неверный или недействительный пользователь и/или пароль"
      }
    }*/

    public String code;
    public String message;
    public int httpCode;
}
