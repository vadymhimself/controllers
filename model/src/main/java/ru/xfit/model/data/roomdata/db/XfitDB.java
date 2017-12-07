package ru.xfit.model.data.roomdata.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import ru.xfit.model.data.roomdata.model.DiaryItem;

/**
 * Created by TESLA on 07.12.2017.
 */
@Database(entities = {DiaryItem.class}, version = 1)
public abstract class XfitDB extends RoomDatabase {
    public abstract DiaryDAO diaryDao();
}
