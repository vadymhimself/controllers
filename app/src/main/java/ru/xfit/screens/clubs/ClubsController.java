package ru.xfit.screens.clubs;

import android.databinding.Bindable;

import com.controllers.Request;

import java.util.ArrayList;
import java.util.List;

import com.android.databinding.library.baseAdapters.BR;

import ru.xfit.R;
import ru.xfit.databinding.LayoutClubsBinding;
import ru.xfit.misc.adapters.BaseAdapter;
import ru.xfit.model.data.schedule.ScheduleClub;
import ru.xfit.model.service.Api;
import ru.xfit.screens.XFitController;

/**
 * Created by TESLA on 06.11.2017.
 */

public class ClubsController extends XFitController<LayoutClubsBinding> {
    private List<ClubVM> vms = new ArrayList<>();

    @Bindable
    public BaseAdapter adapter;

    public ClubsController() {
        Request.with(this, Api.class)
                .create(Api::getMySchedule)
                .onError(error -> {
//                    Snackbar.make(view, "Ошибка: " + error.getMessage(), BaseTransientBottomBar.LENGTH_LONG).show();
                })
                .execute(scheduleListResponse -> {
                    addClubs(scheduleListResponse.schedules);
                });
    }

    public void addClubs(List<ScheduleClub> scheduleClubs) {
        if (scheduleClubs == null || scheduleClubs.size() == 0) {
            if (adapter == null) {
                adapter = new BaseAdapter<>(new ArrayList<>());
                notifyPropertyChanged(BR.adapter);
            }
            return;
        }
        for (ScheduleClub scheduleClub : scheduleClubs) {
            vms.add(new ClubVM(scheduleClub, this));
        }

        adapter = new BaseAdapter<>(vms);
        notifyPropertyChanged(BR.adapter);
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_clubs;
    }
}
