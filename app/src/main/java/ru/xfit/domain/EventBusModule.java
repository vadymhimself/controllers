package ru.xfit.domain;

import com.hwangjr.rxbus.Bus;
import com.hwangjr.rxbus.thread.ThreadEnforcer;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;


@Module
class EventBusModule {

    @Provides
    @AppScope
    Bus provideMainBus() {
        return new Bus(ThreadEnforcer.MAIN);
    }
}
