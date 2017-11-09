package ru.xfit.misc.utils;

import android.util.Log;

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

            DateTime dateYoda = new DateTime(date);

            return currentDate.getDayOfMonth() == dateYoda.getDayOfMonth() && currentDate.getMonthOfYear() == dateYoda.getMonthOfYear();
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static DateTime dateStringToDateTime(String dateString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            Date date = dateFormat.parse(dateString);
            DateTime dateYoda = new DateTime(date);

            return dateYoda;

        } catch (ParseException e) {
            e.printStackTrace();
            return new DateTime(0);
        }
    }
}
