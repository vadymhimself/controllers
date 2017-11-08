package ru.xfit.screens.schedule;

import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;
import com.controllers.Request;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ru.xfit.MainActivity;
import ru.xfit.R;
import ru.xfit.databinding.LayoutClubClassesBinding;
import ru.xfit.misc.adapters.BaseAdapter;
import ru.xfit.misc.adapters.FilterableAdapter;
import ru.xfit.misc.adapters.filters.Filter;
import ru.xfit.model.data.schedule.Activity;
import ru.xfit.model.data.schedule.Schedule;
import ru.xfit.model.data.schedule.Trainer;
import ru.xfit.model.service.Api;

/**
 * Created by TESLA on 04.11.2017.
 */

public class ClubClassesController extends BaseScheduleController<LayoutClubClassesBinding> {

    Set<Trainer> trainers = new HashSet<>();
    Set<Activity> trainings = new HashSet<>();

    @Bindable
    public FilterableAdapter adapter;

    public ClubClassesController(String clubId) {
        Request.setDefaultErrorAction(Throwable::printStackTrace);
        Request.with(this, Api.class)
                .create(api -> api.getClasses(clubId))
                .execute(scheduleListResponse -> {
                    addSchedule(scheduleListResponse.schedule);

                    setTitle(scheduleListResponse.club.title);
//                    ((MainActivity)getActivity()).setTitle(scheduleListResponse.club.title);
//                    ((MainActivity)getActivity()).showHamburgerIcon(true);
                });
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_club_classes;
    }

    public void addSchedule(List<Schedule> schedules) {
        for (Schedule schedule : schedules) {
            trainings.add(schedule.activity);
            for (Trainer trainer : schedule.trainers)
                trainers.add(trainer);

            vms.add(new MyScheduleVM(schedule, this));
        }


        adapter = new FilterableAdapter<>(vms);
        notifyPropertyChanged(BR.adapter);

    }

    public List<Trainer> getTrainers() {
        return new ArrayList<>(trainers);
    }

    public List<Activity> getActivities() {
        return new ArrayList<>(trainings);
    }

    public void updateByFilter(List<Filter> filters) {
        adapter.filter(filters);
    }

}
