package ru.xfit.screens.filter;

import android.databinding.Bindable;
import android.util.Log;
import android.view.View;
import ru.xfit.R;
import ru.xfit.databinding.LayoutFilterBinding;
import ru.xfit.misc.adapters.BaseAdapter;
import ru.xfit.misc.adapters.BaseVM;
import ru.xfit.model.data.schedule.Activity;
import ru.xfit.model.data.schedule.Trainer;
import ru.xfit.screens.XFitController;
import ru.xfit.screens.schedule.FilterListener;

import java.util.*;

/**
 * Created by TESLA on 06.11.2017.
 */

public class FilterController extends XFitController<LayoutFilterBinding> {
    private Collection<Trainer> trainers;
    private Collection<Activity> classes;

    private Set<Trainer> selectedTrainers = new HashSet<>();
    private Set<Activity> selectedActivities = new HashSet<>();

    private final FilterListener listener;

    @Bindable
    public final BaseAdapter<BaseVM> adapter = new BaseAdapter<>(new ArrayList<>());

    public FilterController(FilterListener listener, Collection<Trainer> trainers, Collection<Activity> classes) {
        this.listener = listener;
        this.trainers = trainers;
        this.classes = classes;

        showClasses();
        selectedTrainers.addAll(this.trainers);
        selectedActivities.addAll(this.classes);
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

    private void showTrainers() {
        List<BaseVM> vms = new ArrayList<>();
        vms.add(new HeaderFilterVM(this, true, "Все тренера"));

        for (Trainer trainer : trainers) {
            vms.add(new FilterTrainersVM(this, trainer));
        }

        selectedActivities.clear();
        saveIntermediateSelectionState();

        adapter.clear();
        adapter.addAll(vms);
    }

    private void showClasses() {
        List<BaseVM> vms = new ArrayList<>();

        vms.add(new HeaderFilterVM(this, false, "Все занятия"));

        for (Activity training : classes) {
            vms.add(new FilterClassesVM(this, training));
        }

        selectedTrainers.clear();
        saveIntermediateSelectionState();

        adapter.clear();
        adapter.addAll(vms);
    }

    public void accept(View view){
        onBackPressed();
    }

    private void saveIntermediateSelectionState() {
        for (BaseVM vm : adapter.getItems()) {
            if (vm instanceof FilterTrainersVM) {
                if (((FilterTrainersVM)vm).isChecked)
                    selectedTrainers.add(((FilterTrainersVM)vm).trainer);
            } else if (vm instanceof FilterClassesVM) {
                if (((FilterClassesVM)vm).isChecked)
                    selectedActivities.add(((FilterClassesVM)vm).training);
            }
        }
    }

    @Override
    protected boolean onBackPressed() {
        if(getPrevious() == null)
            return true;

        for (BaseVM vm : adapter.getItems()) {
            if (vm instanceof FilterTrainersVM) {
                if (((FilterTrainersVM)vm).isChecked)
                    selectedTrainers.add(((FilterTrainersVM)vm).trainer);
            } else if (vm instanceof FilterClassesVM) {
                if (((FilterClassesVM)vm).isChecked)
                    selectedActivities.add(((FilterClassesVM)vm).training);
            }
        }

        listener.onUpdate(selectedActivities, selectedTrainers);

        return super.onBackPressed();
    }
}
