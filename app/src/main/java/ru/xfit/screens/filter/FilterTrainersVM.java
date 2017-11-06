package ru.xfit.screens.filter;

import ru.xfit.R;
import ru.xfit.misc.adapters.BaseVM;
import ru.xfit.model.data.schedule.Trainer;

/**
 * Created by TESLA on 06.11.2017.
 */

public class FilterTrainersVM implements BaseVM {
    public FilterController filterController;
    public Trainer trainer;

    public FilterTrainersVM(FilterController filterController, Trainer trainer) {
        this.filterController = filterController;
        this.trainer = trainer;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_filter_trainers;
    }
}
