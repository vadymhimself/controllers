package ru.xfit.misc.utils;

import java.util.Calendar;

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
}
