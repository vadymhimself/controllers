package ru.xfit.screens.filter;

import android.databinding.Bindable;
import android.support.annotation.IntegerRes;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.xfit.R;
import ru.xfit.databinding.LayoutFilterBinding;
import ru.xfit.misc.adapters.BaseAdapter;
import ru.xfit.misc.adapters.BaseVM;
import ru.xfit.model.data.schedule.Trainer;
import ru.xfit.screens.XFitController;

import com.android.databinding.library.baseAdapters.BR;

/**
 * Created by TESLA on 06.11.2017.
 */

public class FilterController extends XFitController<LayoutFilterBinding> {
    public List<Trainer> trainers;
    public List<String> classes;

    private List<BaseVM> vms = new ArrayList<>();

    @Bindable
    public BaseAdapter adapter;

    public FilterController(List<Trainer> trainers) {
        this.trainers = trainers;

        classes = Arrays.asList("Бассейн", "Групповые программы", "Детский фитнес", "Спортивные игры");
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

        if (trainers == null || trainers.size() == 0) {
            if (adapter == null) {
                adapter = new BaseAdapter<>(new ArrayList<>());
                notifyPropertyChanged(BR.adapter);
            }
            return;
        }

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

        if (adapter == null) {
            adapter = new BaseAdapter<>(new ArrayList<>());
            notifyPropertyChanged(BR.adapter);
        }

        vms.add(new HeaderFilterVM(this, false, "Все занятия"));

        for (String training : classes) {
            vms.add(new FilterClassesVM(this, training));
        }

        adapter = new BaseAdapter<>(vms);
        notifyPropertyChanged(BR.adapter);

    }
}
