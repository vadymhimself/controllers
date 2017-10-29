package ru.xfit.model.service;

import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {NetworkModule.class, ApiModule.class, ContextModule.class, PreferencesStorageModule.class})
public interface ModelComponent {
    Api exposeApi();
}
