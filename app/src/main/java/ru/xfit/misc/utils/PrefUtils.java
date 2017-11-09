package ru.xfit.misc.utils;

import android.content.Context;
import android.content.SharedPreferences;
import ru.xfit.domain.App;
import ru.xfit.model.data.storage.preferences.PreferencesManager;

/**
 * Created by TESLA on 25.10.2017.
 */

public class PrefUtils {
    public static final String TAG = PrefUtils.class.getSimpleName();


    public static SharedPreferences getPreferences(){
        return App.getContext().getSharedPreferences(PreferencesManager.KEY_PREFS, Context.MODE_PRIVATE);
    }

    public static void clearPref(){
        getPreferences().edit().clear().apply();
    }
}
