package ru.xfit.screens.schedule;

import ru.xfit.model.data.schedule.Activity;
import ru.xfit.model.data.schedule.Trainer;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by TESLA on 08.11.2017.
 */

public interface FilterListener extends Serializable {
    void onUpdate(Set<Activity> activities, Set<Trainer> trainers);
}
