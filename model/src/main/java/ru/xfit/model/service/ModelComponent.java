package ru.xfit.model.service;

import android.content.Context;

import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {NetworkModule.class, ApiModule.class})
public interface ModelComponent {
    Api exposeApi();
}
