package ru.xfit.screens.schedule;

import android.databinding.Bindable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.android.databinding.library.baseAdapters.BR;
import com.controllers.Request;

import java.util.ArrayList;
import java.util.List;

import ru.xfit.R;
import ru.xfit.databinding.LayoutClubClassesBinding;
import ru.xfit.misc.adapters.BaseAdapter;
import ru.xfit.model.data.schedule.Schedule;
import ru.xfit.model.data.schedule.ScheduleClub;
import ru.xfit.model.service.Api;
import ru.xfit.screens.XFitController;

/**
 * Created by TESLA on 04.11.2017.
 */

public class ClubClassesController extends BaseScheduleController<LayoutClubClassesBinding> {

    @Bindable
    public BaseAdapter adapter;

    public ClubClassesController(String clubId) {
        Request.with(this, Api.class)
                .create(api -> api.getClasses(clubId))
                .onError(error -> {
                    Log.d(">>>>", "Ошибка: " + error.getMessage());
                })
                .execute(scheduleListResponse -> {
                    addSchedule(scheduleListResponse.schedule);
                });
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_club_classes;
    }

    public void addSchedule(List<Schedule> schedules) {
        if (schedules == null || schedules.size() == 0) {
            if (adapter == null) {
                adapter = new BaseAdapter<>(new ArrayList<>());
                notifyPropertyChanged(BR.adapter);
            }
            return;
        }

        for (Schedule schedule : schedules) {
            vms.add(new MyScheduleVM(schedule, this));
        }


        adapter = new BaseAdapter<>(vms);
        notifyPropertyChanged(BR.adapter);

    }

}
