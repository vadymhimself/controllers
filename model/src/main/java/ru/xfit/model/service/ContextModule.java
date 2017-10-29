package ru.xfit.model.service;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.xfit.model.data.UserData;
import ru.xfit.model.data.storage.preferences.PreferencesStorage;
import ru.xfit.model.service.Api;
import ru.xfit.model.service.NetworkInterface;

/**
 * Created by TESLA on 29.10.2017.
 */

@Module
public class ContextModule {
    private Context mContext;

    public ContextModule(Context context) {
        mContext = context;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return mContext;
    }
}
