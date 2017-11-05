package ru.xfit.model.data.schedule;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by TESLA on 03.11.2017.
 */

public class Schedule implements Serializable {
    public String id;
    public String subscriptionId;
    public String datetime;
    public Boolean popular;
    public Integer length;
    public Boolean commercial;
    public Boolean bookingOpened;
    public Boolean preEntry;
    public Boolean firstFree;
    @SerializedName("new")
    public Boolean isNew;
    public Activity activity;
    public List<Trainer> trainers;
    public Item room;
    public Item group;

    public String getLength() {
        return "Продолжительность: " + String.valueOf(length) + " мин";
    }
}
