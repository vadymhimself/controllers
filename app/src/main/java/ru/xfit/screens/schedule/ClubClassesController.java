package ru.xfit.screens.schedule;

import android.databinding.Bindable;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.MenuItem;

import com.hwangjr.rxbus.annotation.Subscribe;
import org.joda.time.DateTime;

import ru.xfit.BR;
import ru.xfit.R;
import ru.xfit.databinding.LayoutClubClassesBinding;
import ru.xfit.misc.adapters.FilterableAdapter;
import ru.xfit.misc.adapters.OnCancelSearchListener;
import ru.xfit.misc.events.OptionsItemSelectedEvent;
import ru.xfit.misc.utils.ListUtils;
import ru.xfit.model.data.schedule.Activity;
import ru.xfit.model.data.schedule.Clazz;
import ru.xfit.model.data.schedule.Schedule;
import ru.xfit.model.data.schedule.Trainer;
import ru.xfit.screens.BlankToolbarController;
import ru.xfit.screens.DateChangeListener;
import ru.xfit.screens.clubs.ClassesFilter;
import ru.xfit.screens.filter.FilterController;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ru.xfit.misc.utils.ListUtils.filter;

/**
 * Created by TESLA on 04.11.2017.
 */

public class ClubClassesController extends BlankToolbarController<LayoutClubClassesBinding>
        implements FilterListener, DateChangeListener, SearchView.OnQueryTextListener, OnCancelSearchListener {

    private final Set<Trainer> trainers = new HashSet<>();
    private final Set<Activity> activities = new HashSet<>();

    private Set<Activity> selectedActivities = new HashSet<>();
    private Set<Trainer> selectedTrainers = new HashSet<>();

    public Schedule schedule;

    private final DayFilter dayFilter = new DayFilter(DateTime.now());
    private final ClassesFilter classesFilter = new ClassesFilter("");

//    private FilterController filterController = new FilterController(this, trainers, activities);

    @Bindable
    public final FilterableAdapter<ClassVM> adapter = new FilterableAdapter<>(new ArrayList<>());

    public ClubClassesController(Schedule schedule) {
        this.schedule = schedule;
        init();
    }

    private void init() {
        adapter.addFilter(dayFilter);
        adapter.addFilter(classesFilter);
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
        this.selectedTrainers = trainers;
        this.selectedActivities = activities;
        adapter.addAll(toAdd);
    }

    @Override
    public void onUpdate(Set<Activity> activities, Set<Trainer> trainers) {
        this.selectedTrainers = trainers;
        this.selectedActivities = activities;
        adapter.refresh();
    }

    @Override
    public String getTitle() {
        return schedule.club.title;
    }

    @Override
    public void onDateChange(DateTime dateTime) {
        dayFilter.setDay(dateTime);
        adapter.refresh();
    }

    @Override
    public void onDatePeriodChanged(DateTime firstSelection, DateTime lastSelection) {

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filter:
                show(new FilterController(this, trainers, activities));
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onNavigationClick() {
        back();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        classesFilter.setTrainerName(query);
        adapter.refresh();

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onCancel() {
        classesFilter.setTrainerName("");
        adapter.refresh();
    }
}
