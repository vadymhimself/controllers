package ru.xfit.misc.utils;

import android.util.MonthDisplayHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import ru.xfit.misc.views.Day;

/**
 * Created by TESLA on 31.10.2017.
 */

public class CalendarUtils {
    public static int getWeekIndex(int weekIndex, Calendar calendar) {
        int firstDayWeekPosition = calendar.getFirstDayOfWeek();
        if (firstDayWeekPosition == 1) {
            return weekIndex;
        } else {
            if (weekIndex == 1) {
                return 7;
            } else {
                return weekIndex - 1;
            }
        }
    }

    public static boolean isSameMonth(Calendar c1, Calendar c2) {
        return !(c1 == null || c2 == null) &&
                (c1.get(Calendar.ERA) == c2.get(Calendar.ERA) &&
                        (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)) &&
                        (c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)));
    }

    public static boolean isToday(Calendar calendar) {
        return isSameDay(calendar, Calendar.getInstance());
    }

    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null)
            throw new IllegalArgumentException("The dates must not be null");
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)) &&
                (cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)));
    }

    public static List<Day> obtainDays(final Calendar calendar, final int index) {
        int year = getYear(calendar);
        int month = getMonth(calendar);
        int firstDayOfWeek = calendar.getFirstDayOfWeek();

        final MonthDisplayHelper helper = new MonthDisplayHelper(year, month, firstDayOfWeek);
        final List<Day> days = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            int n[] = helper.getDigitsForRow(i);

            for (int j = 0; j < 7; j++) {
                Day d = new Day();

                if (helper.isWithinCurrentMonth(i, j)) {
                    Calendar c = Calendar.getInstance(Locale.getDefault());

                    c.add(Calendar.MONTH, index);
                    c.set(Calendar.DAY_OF_MONTH, n[j]);

                    int m = getMonth(c);
                    int y = getYear(c);

                    if (n[j] == c.get(Calendar.DAY_OF_MONTH) && isWeekend(c) && index == 0) {
                        d.setDay(n[j])
                                .setMonth(m)
                                .setYear(y)
                                .setCurrentDay(false)
                                .setCurrentMonth(true)
                                .setCurrentYear(true)
                                .setWeekend(true);

                    } else if (n[j] == c.get(Calendar.DAY_OF_MONTH) && index == 0) {
                        d.setDay(n[j])
                                .setMonth(m)
                                .setYear(y)
                                .setCurrentDay(true)
                                .setCurrentMonth(true)
                                .setCurrentYear(true)
                                .setWeekend(false);

                    } else if (isWeekend(c)) {
                        d.setDay(n[j])
                                .setMonth(m)
                                .setYear(y)
                                .setCurrentDay(false)
                                .setCurrentMonth(true)
                                .setCurrentYear(true)
                                .setWeekend(true);

                    } else {
                        d.setDay(n[j])
                                .setMonth(m)
                                .setYear(y)
                                .setCurrentDay(false)
                                .setCurrentMonth(true)
                                .setCurrentYear(true)
                                .setWeekend(false);
                    }

                } else {
                    Calendar c = Calendar.getInstance(Locale.getDefault());

                    c.add(Calendar.MONTH, index);
                    c.set(Calendar.DAY_OF_MONTH, n[j]);

                    d.setDay(n[j])
                            .setMonth(getMonth(c))
                            .setYear(getYear(c))
                            .setCurrentDay(false)
                            .setCurrentMonth(false)
                            .setCurrentYear(true)
                            .setWeekend(false);
                }

                days.add(d);
            }
        }

        return days;
    }

    public static int getFirstDayOfWeek(Calendar calendar) {
        return calendar.getFirstDayOfWeek();
    }

    public static int getMonth(Calendar calendar) {
        return calendar.get(Calendar.MONTH);
    }

    public static int getYear(Calendar calendar) {
        return calendar.get(Calendar.YEAR);
    }

    public static boolean isWeekend(Calendar calendar) {
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY;
    }
}
