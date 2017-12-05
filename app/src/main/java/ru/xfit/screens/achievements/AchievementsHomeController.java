package ru.xfit.screens.achievements;

import com.controllers.ControllerPagerAdapter;

import ru.xfit.R;
import ru.xfit.databinding.LayoutAchievementsBinding;
import ru.xfit.domain.App;
import ru.xfit.screens.DrawerController;
import ru.xfit.screens.XFitController;
import ru.xfit.screens.achievements.diary.DiaryController;
import ru.xfit.screens.achievements.dynamics.DynamicsController;
import ru.xfit.screens.achievements.impulse.ImpulseController;

/**
 * Created by TESLA on 06.12.2017.
 */

public class AchievementsHomeController extends XFitController<LayoutAchievementsBinding> {

    public final ControllerPagerAdapter pagerAdapter;

    public AchievementsHomeController() {
        pagerAdapter = new ControllerPagerAdapter(this);

        pagerAdapter.addController(new ImpulseController());
        pagerAdapter.addController(new DiaryController());
        pagerAdapter.addController(new DynamicsController());
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_achievements;
    }

    @Override
    public String getTitle() {
        return App.getContext().getResources().getString(R.string.achievements_title);
    }
}
