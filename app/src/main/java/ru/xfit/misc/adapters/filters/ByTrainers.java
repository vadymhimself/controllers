package ru.xfit.misc.adapters.filters;

import java.util.List;

import ru.xfit.misc.adapters.BaseVM;
import ru.xfit.model.data.schedule.Trainer;
import ru.xfit.screens.schedule.MyScheduleVM;

/**
 * Created by TESLA on 08.11.2017.
 */

public class ByTrainers extends Percolator {
    private List<Trainer> trainers;

    public ByTrainers(String title, List<Trainer> trainers) {
        super(title);
        this.trainers = trainers;
    }

    @Override
    public boolean compare(BaseVM vm) {
        if (vm instanceof MyScheduleVM) {
            for (Trainer trainer : trainers) {
                if (((MyScheduleVM)vm).schedule.trainers.contains(trainer))
                    return true;
            }

            return false;
        } else
            return false;
    }
}
