package ru.xfit.model.data.storage.preferences;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

/**
 * Created by TESLA on 28.10.2017.
 */

public final class PreferencesManager {

    public static final String KEY_PREFS = "_app_prefs_ru.xfit_prettyPrefs";
    public static final String KEY_IS_USER_ALREADY_LOGIN = "is_user_already_login";
    private SharedPreferences preferences;

    public PreferencesManager(Context context) {
        this.preferences = context.getApplicationContext()
                .getSharedPreferences(KEY_PREFS, Context.MODE_PRIVATE);
    }

    public String getValue(final String key) {

        return preferences.getString(key, "");
    }

    public void putValue(final String key, final Object value) {

        final SharedPreferences.Editor editor = preferences.edit();
        if (value instanceof Long) {
            editor.putLong(key, (long) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (int) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (boolean) value);
        } else {
            editor.putString(key, value.toString());
        }
        commit(editor);
    }

    public int getInt(final String key, final int defaultValue) {
        return preferences.getInt(key, defaultValue);
    }

    public long getLong(final String key, final long defaultValue) {
        return preferences.getLong(key, defaultValue);
    }

    public boolean getBoolean(final String key) {
        return preferences.getBoolean(key, false);
    }

    public void removeValue(final String key) {
        final SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        commit(editor);
    }

    private void commit(final SharedPreferences.Editor editor) {
        if (Build.VERSION_CODES.GINGERBREAD <= Build.VERSION.SDK_INT) {
            doAPI9StyleCommit(editor);
        } else {
            editor.commit();
        }
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private void doAPI9StyleCommit(final SharedPreferences.Editor editor) {
        editor.apply();
    }
}
