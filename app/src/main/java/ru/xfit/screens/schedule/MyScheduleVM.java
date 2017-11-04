package ru.xfit.screens.schedule;

import android.view.View;

import ru.xfit.R;
import ru.xfit.misc.adapters.BaseVM;
import ru.xfit.model.data.schedule.Schedule;

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
}
