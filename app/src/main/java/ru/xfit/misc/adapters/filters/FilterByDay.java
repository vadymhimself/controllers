package ru.xfit.misc.adapters.filters;

import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ru.xfit.misc.adapters.BaseVM;
import ru.xfit.screens.schedule.MyScheduleVM;

/**
 * Created by TESLA on 08.11.2017.
 */

public class FilterByDay implements Filter {
    private DateTime currentDay;

    public FilterByDay(DateTime currentDay) {
        this.currentDay = currentDay;
    }

    @Override
    public boolean compare(BaseVM vm) {
        if (vm instanceof MyScheduleVM) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                Date date = dateFormat.parse((((MyScheduleVM) vm).schedule.datetime));
                SimpleDateFormat month = new SimpleDateFormat("MM");
                SimpleDateFormat dayMonth = new SimpleDateFormat("dd");

                if (currentDay.getDayOfMonth() == Integer.valueOf(dayMonth.format(date)) &&
                        currentDay.getMonthOfYear() == Integer.valueOf(month.format(date))) {
                    return true;
                } else
                    return false;
            } catch (ParseException e) {
                e.printStackTrace();
                return false;
            }
        } else
            return false;
    }
}
