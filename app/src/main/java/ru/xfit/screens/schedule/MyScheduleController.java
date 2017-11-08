package ru.xfit.screens.schedule;

import android.databinding.Bindable;
import android.view.View;
import com.controllers.Request;
import org.joda.time.DateTime;
import ru.xfit.BR;
import ru.xfit.R;
import ru.xfit.StartActivity;
import ru.xfit.databinding.LayoutMyScheduleBinding;
import ru.xfit.misc.adapters.FilterableAdapter;
import ru.xfit.misc.utils.ListUtils;
import ru.xfit.misc.utils.PrefUtils;
import ru.xfit.model.data.schedule.Clazz;
import ru.xfit.model.data.schedule.Schedule;
import ru.xfit.model.service.Api;
import ru.xfit.screens.DrawerController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static ru.xfit.domain.App.PREFS_IS_USER_ALREADY_LOGIN;

/**
 * Created by TESLA on 25.10.2017.
 */

public class MyScheduleController extends DrawerController<LayoutMyScheduleBinding>
        implements OnCalendarListener {

    private final DayFilter dayFilter = new DayFilter(DateTime.now());

    @Bindable
    public final FilterableAdapter<ClassVM> adapter = new FilterableAdapter<>(new ArrayList<>());

    public MyScheduleController() {
        adapter.addFilter(dayFilter);
        Request.with(this, Api.class)
                .create(api -> api.getMySchedule(DateTime.now().getYear(), DateTime.now().getWeekOfWeekyear()))
                .execute(scheduleListResponse -> {
                    addSchedule(scheduleListResponse.schedules);
                });
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_my_schedule;
    }

    public void logOut() {
        PrefUtils.getPreferences().edit().putBoolean(PREFS_IS_USER_ALREADY_LOGIN, false).apply();
        StartActivity.start(getActivity());
        getActivity().finish();
    }

    private void addSchedule(List<Schedule> scheduleClubs) {
        List<ClassVM> vms = new ArrayList<>();

        for (Schedule schedule : scheduleClubs) {
            for (Clazz clazz : schedule.schedule) {
                vms.add(new ClassVM(clazz, this));
            }
        }

        adapter.addAll(vms);
//        notifyPropertyChanged(BR.highlightedDays);
    }

    public void showClubClassesController(View view) {
        // TODO: show progress
        Request.with(this, Api.class)
                .create(api -> api.getClassesForClub("181"))
                .execute(schedule -> show(new ClubClassesController(schedule)));
    }

    public void services(View view) {

    }

    public List<DateTime> getClassDates() {
        List<DateTime> dates = new ArrayList<>();
        // все желтое починить
        for (Clazz clazz : ListUtils.map(adapter.getAllItems(), it -> it.clazz)) {
            // return
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                Date date = dateFormat.parse(clazz.datetime);
                SimpleDateFormat year = new SimpleDateFormat("yyyy");
                year.format(date);
                SimpleDateFormat month = new SimpleDateFormat("MM");
                month.format(date);
                SimpleDateFormat day = new SimpleDateFormat("dd");
                day.format(date);

                dates.add(new DateTime().withDate(Integer.valueOf(year.format(date)),
                        Integer.valueOf(month.format(date)),
                        Integer.valueOf(day.format(date))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return dates;
    }

    @Override
    public String getTitle() {
        return "Мое расписание";
    }

    @Override
    public void onDateChange(DateTime dateTime) {
        dayFilter.setDay(dateTime);
        adapter.refresh();

        notifyPropertyChanged(BR.adapter);
    }
}
