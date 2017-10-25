package ru.xfit.model.service;

import dagger.Module;
import dagger.Provides;
import ru.xfit.model.data.UserData;

import javax.inject.Singleton;

@Module
class ApiModule {

    @Provides
    @Singleton
    Api provideApi(NetworkInterface networkInterface, UserData userData) {
        return new ApiImpl(networkInterface, userData);
    }
}
