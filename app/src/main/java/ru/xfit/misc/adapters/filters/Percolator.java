package ru.xfit.misc.adapters.filters;

import ru.xfit.misc.adapters.BaseVM;
import ru.xfit.model.data.schedule.Schedule;

/**
 * Created by TESLA on 07.11.2017.
 */

public abstract class Percolator {

    protected String title;

    public Percolator(String title) {
        this.title = title;
    }

    public abstract boolean compare(BaseVM vm);
}
