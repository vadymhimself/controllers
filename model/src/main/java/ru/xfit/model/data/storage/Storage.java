package ru.xfit.model.data.storage;

import ru.xfit.model.data.auth.User;
import ru.xfit.model.data.notification.NotificationSettings;

/**
 * Created by TESLA on 28.10.2017.
 */

public interface Storage {
    User getCurrentUser();
    String saveCurrentUser(User user);
    String clearCurrentUser();

    NotificationSettings getSettings();
    String saveSettings(NotificationSettings settings);
}
