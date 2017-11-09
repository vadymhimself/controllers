package ru.xfit.screens.schedule;

import org.joda.time.DateTime;
import ru.xfit.misc.adapters.PredicateFilter;
import ru.xfit.misc.utils.CalendarUtils;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by TESLA on 08.11.2017.
 */

public class DayFilter extends PredicateFilter<ClassVM> implements Serializable {

    private DateTime day;

    public DayFilter(DateTime day) {
        this.day = day;
    }

    public void setDay(DateTime currentDay) {
        this.day = currentDay;
    }

    @Override
    protected boolean call(ClassVM it) {
        return CalendarUtils.sameDay(day, it.clazz.datetime);
    }
}
