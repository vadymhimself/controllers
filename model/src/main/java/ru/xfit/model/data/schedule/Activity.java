package ru.xfit.model.data.schedule;

import java.io.Serializable;

/**
 * Created by TESLA on 03.11.2017.
 */

public class Activity implements Serializable {
    public String id;
    public String title;
    public String type;
    public String typeId;
    public String url;
    public String color;
    public String description;
    public Integer length;
}
