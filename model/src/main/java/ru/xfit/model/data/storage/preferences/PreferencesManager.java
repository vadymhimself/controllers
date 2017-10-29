package ru.xfit.model.data.storage.preferences;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

/**
 * Created by TESLA on 28.10.2017.
 */

public final class PreferencesManager {
    public static String getValue(final Context context, final String fileName, final String key) {

        return getPreferences(context, fileName).getString(key, "");
    }

    public static void putValue(final Context context, final String fileName, final String key, final Object value) {

        final SharedPreferences.Editor editor = getPreferences(context, fileName).edit();
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

    public static int getInt(final Context context, final String fileName, final String key, final int defaultValue) {

        return getPreferences(context, fileName).getInt(key, defaultValue);
    }

    public static long getLong(final Context context, final String fileName, final String key, final long defaultValue) {

        return getPreferences(context, fileName).getLong(key, defaultValue);
    }

    public static boolean getBoolean(final Context context, final String fileName, final String key, final boolean defaultValue) {

        return getPreferences(context, fileName).getBoolean(key, defaultValue);
    }

    public static void removeValue(final Context context, final String fileName, final String key) {

        final SharedPreferences.Editor editor = getPreferences(context, fileName).edit();
        editor.remove(key);
        commit(editor);
    }

    private static SharedPreferences getPreferences(final Context context, final String fileName) {

        return context.getApplicationContext().getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }

    private static void commit(final SharedPreferences.Editor editor) {

        if (Build.VERSION_CODES.GINGERBREAD <= Build.VERSION.SDK_INT) {
            doAPI9StyleCommit(editor);
        } else {
            editor.commit();
        }
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private static void doAPI9StyleCommit(final SharedPreferences.Editor editor) {
        editor.apply();
    }
}
