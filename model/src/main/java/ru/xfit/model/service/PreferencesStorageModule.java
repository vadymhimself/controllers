package ru.xfit.model.service;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.xfit.model.data.storage.preferences.PreferencesStorage;

/**
 * Created by TESLA on 29.10.2017.
 */

@Module
public class PreferencesStorageModule {
    @Provides
    @Singleton
    PreferencesStorage providePreferencesStorage(Context context) {
        return new PreferencesStorage(context);
    }
}
