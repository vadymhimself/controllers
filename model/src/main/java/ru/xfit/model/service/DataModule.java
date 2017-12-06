package ru.xfit.model.service;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

/**
 * Created by TESLA on 07.12.2017.
 */

@Module
public class DataModule {
    @Provides
    @Singleton
    Realm provideRealm(Context context) {
        Realm.init(context);
        return Realm.getDefaultInstance();
    }
}
