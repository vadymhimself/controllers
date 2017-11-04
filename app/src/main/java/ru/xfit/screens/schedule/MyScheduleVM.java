package ru.xfit.screens.schedule;

import android.graphics.Color;
import android.util.Log;
import android.view.View;

import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ru.xfit.R;
import ru.xfit.misc.adapters.BaseVM;
import ru.xfit.model.data.schedule.Schedule;
import ru.xfit.model.data.schedule.Trainer;

/**
 * Created by TESLA on 04.11.2017.
 */

public class MyScheduleVM implements BaseVM {
    public BaseScheduleController myScheduleController;
    public Schedule schedule;

    public MyScheduleVM(Schedule schedule, BaseScheduleController myScheduleController){
        this.schedule = schedule;
        this.myScheduleController = myScheduleController;
    }

    public void onItemClick(View view){

    }
    @Override
    public int getLayoutId() {
        return R.layout.item_schedule;
    }

    public String getDescription() {
        StringBuilder trainersString = new StringBuilder();
        trainersString.append(schedule.room.title);
        trainersString.append("\n");
        for (int i = 0; i < schedule.trainers.size(); i++) {
            trainersString.append(schedule.trainers.get(i).title);
            if (i < schedule.trainers.size())
                trainersString.append(", ");
        }

        return trainersString.toString();
    }

    public String getTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        try {
            Date date = dateFormat.parse(schedule.datetime);
            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm");
            return formatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public int getColor() {
        if (schedule.activity.color != null)
            return Color.parseColor(schedule.activity.color);
        else
            return Color.parseColor("#ff0000");
    }
}
