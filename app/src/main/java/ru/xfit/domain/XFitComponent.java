package ru.xfit.domain;

import android.content.Context;

import dagger.Component;
import ru.xfit.model.service.ModelComponent;

@AppScope
@Component(dependencies = {ModelComponent.class}, modules = {EventBusModule.class})
interface XFitComponent {
    void inject(App app);
}
