package ru.xfit.screens.schedule;

import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.util.Log;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;
import com.controllers.Request;

import java.util.ArrayList;
import java.util.List;

import ru.xfit.R;
import ru.xfit.databinding.LayoutMyScheduleBinding;
import ru.xfit.misc.OnViewReadyListener;
import ru.xfit.misc.adapters.BaseAdapter;
import ru.xfit.misc.adapters.BaseVM;
import ru.xfit.model.data.schedule.Schedule;
import ru.xfit.model.data.schedule.ScheduleClub;
import ru.xfit.model.service.Api;
import ru.xfit.screens.XFitController;

/**
 * Created by TESLA on 25.10.2017.
 */

public class MyScheduleController extends BaseScheduleController<LayoutMyScheduleBinding>  implements OnViewReadyListener {

    public ObservableField<String> year = new ObservableField<>();
    public ObservableField<String> week = new ObservableField<>();

    @Bindable
    public BaseAdapter adapter;

    public MyScheduleController() {
        Request.with(this, Api.class)
                .create(api -> api.getMySchedule(year.get(), week.get()))
                .onError(error -> {
//                    errorResponse.set(error.getMessage());
//                    Snackbar.make(view, "Ошибка: " + error.getMessage(), BaseTransientBottomBar.LENGTH_LONG).show();
                })
                .execute(scheduleListResponse -> {
                    addSchedule(scheduleListResponse.schedules);
                });

    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_my_schedule;
    }

    @Override
    public void onReady(View root) {
        if (getBinding() == null)
            return;
    }

    public void addSchedule(List<ScheduleClub> scheduleClubs) {
        if (scheduleClubs == null || scheduleClubs.size() == 0) {
            if (adapter == null) {
                adapter = new BaseAdapter<>(new ArrayList<>());
                notifyPropertyChanged(BR.adapter);
            }
            return;
        }
        for (ScheduleClub scheduleClub : scheduleClubs) {
            for (Schedule schedule : scheduleClub.schedule) {
                vms.add(new MyScheduleVM(schedule, this));
            }
        }

        adapter = new BaseAdapter<>(vms);
        notifyPropertyChanged(BR.adapter);

    }

    public void classes(View view) {
        show(new ClubClassesController("181"));

    }

    public void services(View view) {

    }
}
