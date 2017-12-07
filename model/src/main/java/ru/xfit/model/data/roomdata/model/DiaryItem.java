package ru.xfit.model.data.roomdata.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by TESLA on 07.12.2017.
 */

@Entity(tableName = "diary")
public class DiaryItem implements Serializable {
    @PrimaryKey
    private int did;

    @ColumnInfo(name = "mass")
    private long mass;

    @ColumnInfo(name = "dateTime")
    private String dateTime;

    public int getDid() {
        return did;
    }

    public void setDid(int uid) {
        this.did = uid;
    }

    public long getMass() {
        return mass;
    }

    public void setMass(long mass) {
        this.mass = mass;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
