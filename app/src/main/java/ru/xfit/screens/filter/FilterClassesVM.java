package ru.xfit.screens.filter;

import ru.xfit.R;
import ru.xfit.misc.adapters.BaseVM;

/**
 * Created by TESLA on 06.11.2017.
 */

public class FilterClassesVM implements BaseVM {
    public FilterController filterController;
    public String training;

    public FilterClassesVM(FilterController filterController, String training) {
        this.filterController = filterController;
        this.training = training;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_filter_classes;
    }
}
