package ru.xfit.screens.schedule;

import org.joda.time.DateTime;

import java.io.Serializable;

/**
 * Created by TESLA on 08.11.2017.
 */

public interface OnCalendarListener extends Serializable {
    void onDateChange(DateTime dateTime);
}
