package ru.xfit.domain;

import dagger.Component;
import ru.xfit.model.service.ModelComponent;

@AppScope
@Component(dependencies = {ModelComponent.class})
interface XFitComponent {
    void inject(App app);
    void inject(MainActivity activity);
}
