package ru.xfit.screens.clubs;

import ru.xfit.R;
import ru.xfit.misc.adapters.BaseVM;
import ru.xfit.model.data.schedule.Trainer;

/**
 * Created by TESLA on 15.11.2017.
 */

public class TrainerVM implements BaseVM {
    public Trainer trainer;
    public TrainersController controller;

    public TrainerVM(Trainer trainer, TrainersController controller) {
        this.trainer = trainer;
        this.controller = controller;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_trainer;
    }
}
