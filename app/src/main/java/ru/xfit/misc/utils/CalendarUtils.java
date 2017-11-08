package ru.xfit.misc.utils;

import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by TESLA on 09.11.2017.
 */

public class CalendarUtils {

    public static boolean sameDay(DateTime currentDate, String dateTime) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            Date date = dateFormat.parse(dateTime);

            currentDate.toDate();
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }
}
