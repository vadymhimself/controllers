package ru.xfit.misc.adapters.filters;

import java.util.List;

import ru.xfit.misc.adapters.BaseVM;
import ru.xfit.screens.schedule.MyScheduleVM;

/**
 * Created by TESLA on 08.11.2017.
 */

public class ByClassType extends Percolator {
    private List<String> classes;

    public ByClassType(String title, List<String> classes) {
        super(title);
        this.classes = classes;
    }

    @Override
    public boolean compare(BaseVM vm) {
        if (vm instanceof MyScheduleVM) {
            for (String activity : classes) {
                if (((MyScheduleVM)vm).schedule.activity.id.equals(activity))
                    return true;
            }

            return false;
        } else
            return false;
    }
}
