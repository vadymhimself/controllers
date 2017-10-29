package ru.xfit.model.data.club;

import java.io.Serializable;

/**
 * Created by TESLA on 25.10.2017.
 */

public class Class implements Serializable {
    /*{
        "id": 0,
        "classTypeId": 0,
        "date": "2017-10-25",
        "beginTime": "string",
        "durationMinutes": 0,
        "masterId": 0,
        "roomId": 0,
        "capacity": 0,
        "color": "string"
      }*/

    public int id;
    public int classTypeId;
    public String date;
    public String beginTime;
    public int durationMinutes;
    public int masterId;
    public int roomId;
    public int capacity;
    public String color;
}
