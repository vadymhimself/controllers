package ru.xfit.misc.adapters.filters;

import java.util.List;

import ru.xfit.misc.adapters.BaseVM;
import ru.xfit.model.data.schedule.Activity;
import ru.xfit.screens.schedule.MyScheduleVM;

/**
 * Created by TESLA on 08.11.2017.
 */

public class FilterByClassType implements Filter {
    private List<Activity> classes;

    public FilterByClassType(List<Activity> classes) {
        this.classes = classes;
    }

    @Override
    public boolean compare(BaseVM vm) {
        if (vm instanceof MyScheduleVM) {
            for (Activity activity : classes) {
                if (((MyScheduleVM)vm).schedule.activity.id.equals(activity.id))
                    return true;
            }

            return false;
        } else
            return false;
    }
}
