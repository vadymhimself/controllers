package ru.xfit.model.data.schedule;

import java.io.Serializable;
import java.util.List;

import ru.xfit.model.data.club.Club;

/**
 * Created by TESLA on 03.11.2017.
 */

public class ScheduleClub implements Serializable {
    public Club club;
    public List<Schedule> schedule = null;
}
