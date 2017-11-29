package ru.xfit.model.data.notifications;

import java.io.Serializable;

/**
 * Created by TESLA on 29.11.2017.
 */

public class NotificationSettingsRequest implements Serializable {
    public String notificationType;

    public NotificationSettingsRequest(String notificationType) {
        this.notificationType = notificationType;
    }
}
