package ru.xfit.model.data.roomdata.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import ru.xfit.model.data.roomdata.model.DiaryItem;

/**
 * Created by TESLA on 07.12.2017.
 */

@Dao
public interface DiaryDAO extends Serializable {
    @Query("SELECT * FROM diary")
    List<DiaryItem> getAll();

    @Query("SELECT * FROM diary WHERE dateTime LIKE :month")
    List<DiaryItem> loadAllByDates(String month);

    @Query("SELECT * FROM diary WHERE dateTime LIKE :dt LIMIT 1")
    DiaryItem findByDateTime(String dt);

    @Query("UPDATE diary SET mass = :mass WHERE dateTime LIKE :dateTime")
    int updateDiary(String dateTime, long mass);

    @Insert
    void insertAll(DiaryItem... items);

    @Delete
    void delete(DiaryItem diaryItem);


}
