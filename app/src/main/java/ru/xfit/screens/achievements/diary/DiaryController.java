package ru.xfit.screens.achievements.diary;

import com.controllers.ControllerPagerAdapter;
import com.controllers.Request;

import org.joda.time.DateTime;

import ru.xfit.R;
import ru.xfit.databinding.LayoutDiaryBinding;
import ru.xfit.domain.App;
import ru.xfit.model.service.Api;
import ru.xfit.screens.XFitController;

/**
 * Created by TESLA on 06.12.2017.
 */

public class DiaryController extends XFitController<LayoutDiaryBinding> {

    public ControllerPagerAdapter pagerAdapter;
    private final DateTime today = DateTime.now();

    public DiaryController() {

        Request.with(this, Api.class)
                .create(api -> api.getDiaryItems(today.toString("MMM")))
                .execute(result -> {
                    //get all diary item from month
                    //check
                });
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_diary;
    }

    @Override
    public String getTitle() {
        return App.getContext().getResources().getString(R.string.achievements_diary_title);
    }
}
