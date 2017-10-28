package ru.xfit.model.data.storage.preferences;

import android.content.Context;

import com.google.gson.Gson;

import javax.inject.Inject;

import ru.xfit.model.data.auth.User;
import ru.xfit.model.data.storage.Storage;

/**
 * Created by TESLA on 28.10.2017.
 */

public class PreferencesStorage implements Storage {
    private static final String FILE_NAME = "_app_prefs_ru.xfit_prettyPrefs";

    private static final String KEY_CURRENT_USER = "KEY_CURRENT_USER";
    private static final String KEY_SETTINGS = "KEY_SETTINGS";
    private static final String KEY_CONFIG = "KEY_CONFIG";

    private Context mContext;
    private Gson mGson;

    @Inject
    public PreferencesStorage(Context context) {
        mContext = context;
        mGson = new Gson();
    }

    @Override
    public User getCurrentUser() {
        User currentUser = mGson.fromJson(PreferencesManager.getValue(mContext, FILE_NAME, KEY_CURRENT_USER), User.class);
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
        PreferencesManager.putValue(mContext, FILE_NAME, KEY_CURRENT_USER, mGson.toJson(user));
        return "saved";
    }

    @Override
    public String clearCurrentUser() {
        PreferencesManager.removeValue(mContext, FILE_NAME, KEY_CURRENT_USER);
        return "deleted";
    }
}
