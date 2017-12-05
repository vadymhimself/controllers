package ru.xfit.screens.achievements.dynamics;

import ru.xfit.R;
import ru.xfit.databinding.LayoutDynamicsBinding;
import ru.xfit.domain.App;
import ru.xfit.screens.XFitController;

/**
 * Created by TESLA on 06.12.2017.
 */

public class DynamicsController extends XFitController<LayoutDynamicsBinding> {
    @Override
    public int getLayoutId() {
        return R.layout.layout_dynamics;
    }

    @Override
    public String getTitle() {
        return App.getContext().getResources().getString(R.string.achievements_dynamics_title);
    }
}
