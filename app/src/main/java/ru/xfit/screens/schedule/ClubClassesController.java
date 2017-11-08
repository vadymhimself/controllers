package ru.xfit.screens.schedule;

import android.databinding.Bindable;
import com.hwangjr.rxbus.annotation.Subscribe;
import org.joda.time.DateTime;
import ru.xfit.R;
import ru.xfit.databinding.LayoutClubClassesBinding;
import ru.xfit.misc.OptionsItemSelectedEvent;
import ru.xfit.misc.adapters.FilterableAdapter;
import ru.xfit.model.data.schedule.Activity;
import ru.xfit.model.data.schedule.Clazz;
import ru.xfit.model.data.schedule.Schedule;
import ru.xfit.model.data.schedule.Trainer;
import ru.xfit.screens.filter.FilterController;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ru.xfit.misc.utils.ListUtils.filter;

/**
 * Created by TESLA on 04.11.2017.
 * TODO: download more classes in itself
 * TODO: тут вообще-то календарь тоже будет, и фильтр по дням...
 */

public class ClubClassesController extends BaseScheduleController<LayoutClubClassesBinding>
        implements FilterListener {

    private final Set<Trainer> trainers = new HashSet<>();
    private final Set<Activity> activities = new HashSet<>();

    private Set<Activity> selectedActivities = new HashSet<>();
    private Set<Trainer> selectedTrainers = new HashSet<>();

    private final DayFilter dayFilter = new DayFilter(DateTime.now());

    @Bindable
    public final FilterableAdapter<ClassVM> adapter = new FilterableAdapter<>(new ArrayList<>());

    ClubClassesController(Schedule schedule) {
        adapter.addFilter(dayFilter);
        // class type and trainer filters
        adapter.addFilter(list -> filter(list, it -> selectedActivities.contains(it.clazz.activity)));
        adapter.addFilter(list -> filter(list, it -> {
            for (Trainer t : it.clazz.trainers) {
                if (selectedTrainers.contains(t)) {
                    return true;
                }
            }
            return false;
        }));
        addClasses(schedule.schedule);
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_club_classes;
    }

    @Subscribe
    public void onOptionsItemSelectedEvent(OptionsItemSelectedEvent e) {
        switch (e.menuItem.getItemId()) {
            case R.id.filter:
                show(new FilterController(this, trainers, activities));
                break;
            default:
                break;
        }

    }

    private void addClasses(List<Clazz> classes) {
        List<ClassVM> toAdd = new ArrayList<>();
        for (Clazz schedule : classes) {
            activities.add(schedule.activity);
            trainers.addAll(schedule.trainers);
            toAdd.add(new ClassVM(schedule, this));
        }
        adapter.addAll(toAdd);
    }

    @Override
    public void onUpdate(Set<Activity> activities, Set<Trainer> trainers) {
        this.selectedTrainers = trainers;
        this.selectedActivities = activities;
        adapter.refresh();
    }
}
