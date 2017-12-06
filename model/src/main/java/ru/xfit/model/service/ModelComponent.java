package ru.xfit.model.service;

import com.hwangjr.rxbus.Bus;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {
        NetworkModule.class,
        ApiModule.class,
        ContextModule.class,
        DataModule.class,
        PreferencesStorageModule.class,
        EventBusModule.class
})
public interface ModelComponent {
    Api exposeApi();
    Bus exposeBus();
}
