package ru.xfit.screens.schedule;

import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.util.Log;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;
import com.controllers.Request;
import com.molo17.customizablecalendar.library.interactors.AUCalendar;

import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Interceptor;
import ru.xfit.R;
import ru.xfit.StartActivity;
import ru.xfit.databinding.LayoutMyScheduleBinding;
import ru.xfit.misc.OnViewReadyListener;
import ru.xfit.misc.adapters.BaseAdapter;
import ru.xfit.misc.adapters.BaseVM;
import ru.xfit.misc.utils.PrefUtils;
import ru.xfit.model.data.schedule.Schedule;
import ru.xfit.model.data.schedule.ScheduleClub;
import ru.xfit.model.service.Api;
import ru.xfit.screens.XFitController;

import static ru.xfit.domain.App.PREFS_IS_USER_ALREADY_LOGIN;
import static ru.xfit.domain.App.getContext;

/**
 * Created by TESLA on 25.10.2017.
 */

public class MyScheduleController extends BaseScheduleController<LayoutMyScheduleBinding>  implements OnViewReadyListener {

    public ObservableField<String> year = new ObservableField<>();
    public ObservableField<String> week = new ObservableField<>();

    public AUCalendar auCalendarInstance;
    private List<DateTime> highlighteDays;

    @Bindable
    public BaseAdapter adapter;

    public MyScheduleController() {
        Request.with(this, Api.class)
                .create(api -> api.getMySchedule(year.get(), week.get()))
                .onError(error -> {
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

    public void logOut() {
        PrefUtils.getPreferences().edit().putBoolean(PREFS_IS_USER_ALREADY_LOGIN, false).commit();
        StartActivity.start(getActivity());
        getActivity().finish();
    }

    public void addSchedule(List<ScheduleClub> scheduleClubs) {
        highlighteDays = new ArrayList<>();
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
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                try {
                    Date date = dateFormat.parse(schedule.datetime);
                    SimpleDateFormat year = new SimpleDateFormat("yyyy");
                    year.format(date);
                    SimpleDateFormat month = new SimpleDateFormat("MM");
                    month.format(date);
                    SimpleDateFormat day = new SimpleDateFormat("dd");
                    day.format(date);

                    highlighteDays.add(new DateTime().withDate(Integer.valueOf(year.format(date)),
                            Integer.valueOf(month.format(date)),
                            Integer.valueOf(day.format(date))));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        auCalendarInstance.highLightDates(highlighteDays);

        adapter = new BaseAdapter<>(vms);
        notifyPropertyChanged(BR.adapter);

    }

    public void updateSchedules() {

        notifyPropertyChanged(BR.adapter);
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

    public void classes(View view) {
        show(new ClubClassesController("181"));

    }

    public void services(View view) {

    }
}
