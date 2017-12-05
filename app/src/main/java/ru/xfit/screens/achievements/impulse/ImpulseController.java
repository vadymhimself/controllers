package ru.xfit.screens.achievements.impulse;

import ru.xfit.R;
import ru.xfit.databinding.LayoutImpulseBinding;
import ru.xfit.domain.App;
import ru.xfit.screens.XFitController;

/**
 * Created by TESLA on 06.12.2017.
 */

public class ImpulseController extends XFitController<LayoutImpulseBinding> {
    @Override
    public int getLayoutId() {
        return R.layout.layout_impulse;
    }

    @Override
    public String getTitle() {
        return App.getContext().getResources().getString(R.string.achievements_impulse_title);
    }
}
