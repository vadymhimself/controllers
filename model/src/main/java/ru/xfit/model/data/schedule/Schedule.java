package ru.xfit.model.data.schedule;

import ru.xfit.model.data.club.Club;

import java.io.Serializable;
import java.util.List;

/**
 * Created by TESLA on 03.11.2017.
 */

public class Schedule implements Serializable {
    public Club club;
    public List<Clazz> schedule;
}
