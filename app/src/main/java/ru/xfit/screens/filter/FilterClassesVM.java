package ru.xfit.screens.filter;

import ru.xfit.R;
import ru.xfit.misc.adapters.BaseVM;
import ru.xfit.model.data.schedule.Activity;

/**
 * Created by TESLA on 06.11.2017.
 */

public class FilterClassesVM extends FilterVM implements BaseVM {
    public FilterController filterController;
    public Activity training;

    public FilterClassesVM(FilterController filterController, Activity training) {
        this.filterController = filterController;
        this.training = training;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_filter_classes;
    }
}
