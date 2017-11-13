package ru.xfit.screens.schedule;

import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.view.View;
import com.controllers.Request;
import org.joda.time.DateTime;
import ru.xfit.BR;
import ru.xfit.R;
import ru.xfit.databinding.LayoutMyScheduleBinding;
import ru.xfit.misc.adapters.FilterableAdapter;
import ru.xfit.misc.utils.CalendarUtils;
import ru.xfit.misc.utils.ListUtils;
import ru.xfit.model.data.schedule.Clazz;
import ru.xfit.model.data.schedule.Schedule;
import ru.xfit.model.service.Api;
import ru.xfit.screens.DrawerController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TESLA on 25.10.2017.
 */

public class MyScheduleController extends DrawerController<LayoutMyScheduleBinding>
        implements OnCalendarListener {

    public final ObservableBoolean progress = new ObservableBoolean();

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

    private void addSchedule(List<Schedule> scheduleClubs) {
        List<ClassVM> vms = new ArrayList<>();

        for (Schedule schedule : scheduleClubs) {
            for (Clazz clazz : schedule.schedule) {
                vms.add(new ClassVM(clazz, this));
            }
        }

        adapter.addAll(vms);
        notifyPropertyChanged(BR.classDates);
    }

    public void showClubClassesController(View view) {
        progress.set(true);
        Request.with(this, Api.class)
                .create(api -> api.getClassesForClub("181"))
                .onFinally(() -> progress.set(false))
                .execute(schedule -> {
                    MyScheduleController.this.show(new ClubClassesController(schedule));
                });
    }

    public void services(View view) {

    }

    @Bindable
    public List<DateTime> getClassDates() {
        return ListUtils.map(adapter.getAllItems(), it -> CalendarUtils.dateStringToDateTime(it.clazz.datetime));
    }

    @Bindable
    public boolean isScheduleEmpty() {
        return adapter.getItemCount() == 0;
    }

    @Override
    public String getTitle() {
        return "Мое расписание";
    }

    @Override
    public void onDateChange(DateTime dateTime) {
        dayFilter.setDay(dateTime);
        adapter.refresh();
        notifyPropertyChanged(BR.scheduleEmpty);
    }
}
