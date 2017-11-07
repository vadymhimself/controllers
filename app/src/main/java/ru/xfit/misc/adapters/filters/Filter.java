package ru.xfit.misc.adapters.filters;

import ru.xfit.misc.adapters.BaseVM;
import ru.xfit.model.data.schedule.Schedule;

/**
 * Created by TESLA on 07.11.2017.
 */

public interface Filter {
    public boolean compare(BaseVM vm);
}
