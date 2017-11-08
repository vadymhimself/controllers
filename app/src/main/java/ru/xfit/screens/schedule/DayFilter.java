package ru.xfit.screens.schedule;

import org.joda.time.DateTime;
import ru.xfit.misc.adapters.PredicateFilter;

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
        try {
            // TODO: переписать по людски и вынести парсинг
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            Date date = dateFormat.parse(it.clazz.datetime));
            SimpleDateFormat month = new SimpleDateFormat("MM");
            SimpleDateFormat dayMonth = new SimpleDateFormat("dd");

            return day.getDayOfMonth() == Integer.valueOf(dayMonth.format(date)) &&
                    day.getMonthOfYear() == Integer.valueOf(month.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return true;
        }
    }
}
