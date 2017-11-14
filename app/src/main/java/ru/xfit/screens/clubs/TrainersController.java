package ru.xfit.screens.clubs;

import android.databinding.Bindable;
import android.databinding.ObservableBoolean;

import com.controllers.Request;

import java.util.ArrayList;
import java.util.List;

import ru.xfit.BR;
import ru.xfit.R;
import ru.xfit.databinding.LayoutTrainersBinding;
import ru.xfit.domain.App;
import ru.xfit.misc.adapters.BaseAdapter;
import ru.xfit.misc.adapters.BaseVM;
import ru.xfit.model.data.club.ClubItem;
import ru.xfit.model.data.schedule.Trainer;
import ru.xfit.model.service.Api;
import ru.xfit.screens.XFitController;

/**
 * Created by TESLA on 15.11.2017.
 */

public class TrainersController extends XFitController<LayoutTrainersBinding> {

    @Bindable
    public final BaseAdapter<BaseVM> adapter = new BaseAdapter<>(new ArrayList<>());
    public final ObservableBoolean progress = new ObservableBoolean();

    private ClubItem club;

    public TrainersController(ClubItem club) {
        this.club = club;

        setTitle(App.getContext().getString(R.string.trainers_fitness_trainers));
        progress.set(true);
        Request.with(this, Api.class)
                .create(api -> api.getTrainers(club.id))
                .onFinally(() -> progress.set(false))
                .execute(this::addTrainers);
    }

    @Bindable
    public boolean isTrainersEmpty() {
        return adapter.getItemCount() == 0;
    }

    private void addTrainers(List<Trainer> trainers) {
        List<BaseVM> toAdd = new ArrayList<>();
        for (Trainer trainer : trainers) {
            toAdd.add(new TrainerVM(trainer, this));
        }
        adapter.addAll(toAdd);
        notifyPropertyChanged(BR.trainersEmpty);
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_trainers;
    }
}
