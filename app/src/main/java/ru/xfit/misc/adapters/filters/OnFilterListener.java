package ru.xfit.misc.adapters.filters;

import java.io.Serializable;
import java.util.List;

import ru.xfit.model.data.schedule.Activity;
import ru.xfit.model.data.schedule.Trainer;

/**
 * Created by TESLA on 08.11.2017.
 */

public interface OnFilterListener extends Serializable {
    void onActivitiesFilter(List<Activity> activities);
    void onTrainersFilter(List<Trainer> trainers);
}
