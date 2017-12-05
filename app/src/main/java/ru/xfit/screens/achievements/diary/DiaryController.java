package ru.xfit.screens.achievements.diary;

import ru.xfit.R;
import ru.xfit.databinding.LayoutDiaryBinding;
import ru.xfit.domain.App;
import ru.xfit.screens.XFitController;

/**
 * Created by TESLA on 06.12.2017.
 */

public class DiaryController extends XFitController<LayoutDiaryBinding> {
    @Override
    public int getLayoutId() {
        return R.layout.layout_diary;
    }

    @Override
    public String getTitle() {
        return App.getContext().getResources().getString(R.string.achievements_diary_title);
    }
}
