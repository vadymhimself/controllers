package ru.xfit.model.service;

import android.arch.persistence.room.Room;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.xfit.model.data.roomdata.db.XfitDB;

/**
 * Created by TESLA on 07.12.2017.
 */

@Module
public class DataModule {
    @Provides
    @Singleton
    XfitDB provideDB(Context context) {
        return Room.databaseBuilder(context,
                XfitDB.class, "xfit.db")
                .build();
    }
}
