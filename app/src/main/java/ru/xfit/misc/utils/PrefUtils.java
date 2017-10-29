package ru.xfit.misc.utils;

import android.content.Context;
import android.content.SharedPreferences;

import ru.xfit.domain.App;

/**
 * Created by TESLA on 25.10.2017.
 */

public class PrefUtils {
    public static final String TAG = PrefUtils.class.getSimpleName();


    public static SharedPreferences getPreferences(){
        return App.getContext().getSharedPreferences(App.PREFERENCES, Context.MODE_PRIVATE);
    }

    public static void clearPref(){
        getPreferences().edit().clear().apply();
    }
}
