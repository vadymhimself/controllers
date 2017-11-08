package ru.xfit.screens.schedule;

import android.databinding.Bindable;
import android.databinding.Observable;
import android.databinding.ObservableField;
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

import ru.xfit.MainActivity;
import ru.xfit.R;
import ru.xfit.StartActivity;
import ru.xfit.databinding.LayoutMyScheduleBinding;
import ru.xfit.misc.OnViewReadyListener;
import ru.xfit.misc.adapters.FilterableAdapter;
import ru.xfit.misc.adapters.filters.Filter;
import ru.xfit.misc.adapters.filters.FilterByClassType;
import ru.xfit.misc.adapters.filters.FilterByDay;
import ru.xfit.misc.adapters.filters.OnCalendarListener;
import ru.xfit.misc.utils.PrefUtils;
import ru.xfit.model.data.schedule.Schedule;
import ru.xfit.model.data.schedule.ScheduleClub;
import ru.xfit.model.service.Api;
import ru.xfit.screens.DrawerController;

import static ru.xfit.domain.App.PREFS_IS_USER_ALREADY_LOGIN;

/**
 * Created by TESLA on 25.10.2017.
 */

public class MyScheduleController extends DrawerController<LayoutMyScheduleBinding>
        implements OnViewReadyListener, OnCalendarListener {

    public ObservableField<String> year = new ObservableField<>();
    public ObservableField<String> week = new ObservableField<>();

    List<Filter> filters = new ArrayList<>();

    private ArrayList<Schedule> allSchedule = new ArrayList<>();

    private ClubClassesController clubClassesController = new ClubClassesController("181");

    @Bindable
    public FilterableAdapter adapter;

    public MyScheduleController() {

        setTitle("Мое расписание");

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
        ((MainActivity)getActivity()).showHamburgerIcon(false);
    }

    public void logOut() {
        PrefUtils.getPreferences().edit().putBoolean(PREFS_IS_USER_ALREADY_LOGIN, false).commit();
        StartActivity.start(getActivity());
        getActivity().finish();
    }

    public void addSchedule(List<ScheduleClub> scheduleClubs) {
        List<DateTime> highlighteDays = new ArrayList<>();
        allSchedule.clear();

        for (ScheduleClub scheduleClub : scheduleClubs) {
            allSchedule.addAll(scheduleClub.schedule);
            for (Schedule schedule : scheduleClub.schedule) {
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

                    vms.add(new MyScheduleVM(schedule, this));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        AUCalendar.getInstance().highLightDates(highlighteDays);

        adapter = new FilterableAdapter<>(vms);
        notifyPropertyChanged(BR.adapter);

//        onDateChange(AUCalendar.getInstance().getToday());

    }

    public void classes(View view) {
        show(clubClassesController);
    }

    public void services(View view) {

    }

    @Override
    public void onDateChange(DateTime dateTime) {
        filters.clear();
        filters.add(new FilterByDay(dateTime));
        adapter.filter(filters);
    }
}
