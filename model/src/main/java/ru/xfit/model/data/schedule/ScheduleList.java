package ru.xfit.model.data.schedule;

import java.io.Serializable;
import java.util.List;

/**
 * Created by TESLA on 03.11.2017.
 */

public class ScheduleList implements Serializable {
    public String dateSince;
    public String dateTo;
    public List<Schedule> schedules;
}
