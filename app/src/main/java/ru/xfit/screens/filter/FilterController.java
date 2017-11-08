package ru.xfit.screens.filter;

import android.databinding.Bindable;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ru.xfit.R;
import ru.xfit.databinding.LayoutFilterBinding;
import ru.xfit.misc.adapters.BaseAdapter;
import ru.xfit.misc.adapters.BaseVM;
import ru.xfit.misc.adapters.filters.FilterByClassType;
import ru.xfit.misc.adapters.filters.FilterByTrainers;
import ru.xfit.misc.adapters.filters.Filter;
import ru.xfit.model.data.schedule.Activity;
import ru.xfit.model.data.schedule.Trainer;
import ru.xfit.screens.XFitController;
import ru.xfit.screens.schedule.ClubClassesController;

import com.android.databinding.library.baseAdapters.BR;

/**
 * Created by TESLA on 06.11.2017.
 */

public class FilterController extends XFitController<LayoutFilterBinding> {
    public List<Trainer> trainers;
    public List<Activity> classes;
    private List<Filter> filters = new ArrayList<>();

    private List<BaseVM> vms = new ArrayList<>();

    @Bindable
    public BaseAdapter adapter;

    public FilterController(List<Trainer> trainers, List<Activity> classes) {
        this.trainers = trainers;
        this.classes = classes;

        showClasses();
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_filter;
    }

    public void checkedIncome(Integer radioId) {
        switch (radioId) {
            case R.id.by_classes:
                showClasses();
                break;
            case R.id.by_trainers:
                showTrainers();
                break;
            case R.id.by_all:
                break;
            case R.id.by_morning:
                break;
            case R.id.by_day:
                break;
            case R.id.by_evening:
                break;
            default:
                Log.d(">>>>", "missed id " + radioId);
                break;
        }
    }

    public void showTrainers() {
        if (vms != null)
            vms.clear();

        vms.add(new HeaderFilterVM(this, true, "Все тренера"));

        for (Trainer trainer : trainers) {
            vms.add(new FilterTrainersVM(this, trainer));
        }

        adapter = new BaseAdapter<>(vms);
        notifyPropertyChanged(BR.adapter);
    }

    public void showClasses() {
        if (vms != null)
            vms.clear();

        vms.add(new HeaderFilterVM(this, false, "Все занятия"));

        for (Activity training : classes) {
            vms.add(new FilterClassesVM(this, training));
        }

        adapter = new BaseAdapter<>(vms);
        notifyPropertyChanged(BR.adapter);

    }

    public void accept(View view){
        onBackPressed();
    }

    @Override
    protected boolean onBackPressed() {
        if(getPrevious() == null)
            return true;
        List<Trainer> selectedTrainers = new ArrayList<>();
        List<Activity> selectedActivities = new ArrayList<>();
        for (BaseVM vm : vms) {
            if (vm instanceof FilterTrainersVM) {
                if (((FilterTrainersVM)vm).isChecked)
                    selectedTrainers.add(((FilterTrainersVM)vm).trainer);
            } else if (vm instanceof FilterClassesVM) {
                if (((FilterClassesVM)vm).isChecked)
                    selectedActivities.add(((FilterClassesVM)vm).training);
            }
        }

        if (selectedTrainers.size() > 0)
            filters.add(new FilterByTrainers(selectedTrainers));
        if (selectedActivities.size() > 0)
            filters.add(new FilterByClassType(selectedActivities));

        ((ClubClassesController)getPrevious()).updateByFilter(filters);

        return super.onBackPressed();
    }
}
