package ru.xfit.model.data.storage.preferences;

import android.content.Context;
import com.google.gson.Gson;
import ru.xfit.model.data.auth.User;
import ru.xfit.model.data.storage.Storage;

import javax.inject.Inject;

/**
 * Created by TESLA on 28.10.2017.
 */

public class PreferencesStorage implements Storage {
    private static final String KEY_CURRENT_USER = "KEY_CURRENT_USER";
    private static final String KEY_SETTINGS = "KEY_SETTINGS";
    private static final String KEY_CONFIG = "KEY_CONFIG";


    private final Gson mGson;

    private final PreferencesManager preferencesManager;

    @Inject
    public PreferencesStorage(Context context) {
        preferencesManager = new PreferencesManager(context);
        mGson = new Gson();
    }

    @Override
    public User getCurrentUser() {
        User currentUser = mGson.fromJson(preferencesManager.getValue(KEY_CURRENT_USER), User.class);
        if (currentUser != null)
            return currentUser;
        else {
            User user = new User();
            user.id = "-1";
            return user;
        }
    }

    @Override
    public String saveCurrentUser(User user) {
        preferencesManager.putValue(KEY_CURRENT_USER, mGson.toJson(user));
        return "saved";
    }

    @Override
    public String clearCurrentUser() {
        preferencesManager.putValue(PreferencesManager.KEY_IS_USER_ALREADY_LOGIN, false);
        preferencesManager.removeValue(KEY_CURRENT_USER);
        return "deleted";
    }
}
