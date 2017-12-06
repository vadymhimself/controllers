package ru.xfit.model.service;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import ru.xfit.model.data.UserData;
import ru.xfit.model.data.storage.Storage;
import ru.xfit.model.data.storage.preferences.PreferencesStorage;

import javax.inject.Singleton;

@Module
class ApiModule {
    @Provides
    @Singleton
    Api provideApi(NetworkInterface networkInterface, UserData userData, PreferencesStorage preferencesStorage, Realm realm) {
        return new ApiImpl(networkInterface, userData, preferencesStorage, realm);
    }
}
