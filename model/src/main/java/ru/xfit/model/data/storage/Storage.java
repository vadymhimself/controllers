package ru.xfit.model.data.storage;

import ru.xfit.model.data.auth.User;

/**
 * Created by TESLA on 28.10.2017.
 */

public interface Storage {
    User getCurrentUser();
    String saveCurrentUser(User user);
    String clearCurrentUser();
}
