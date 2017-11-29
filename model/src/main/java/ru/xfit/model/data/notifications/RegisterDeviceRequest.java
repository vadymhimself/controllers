package ru.xfit.model.data.notifications;

import java.io.Serializable;

/**
 * Created by TESLA on 29.11.2017.
 */

public class RegisterDeviceRequest implements Serializable {
    public  String gcmToken;

    public RegisterDeviceRequest(String gcmToken) {
        this.gcmToken = gcmToken;
    }
}
