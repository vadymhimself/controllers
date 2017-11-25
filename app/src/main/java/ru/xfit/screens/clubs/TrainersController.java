package ru.xfit.screens.clubs;

import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.MenuItem;

import com.controllers.Request;

import java.util.ArrayList;
import java.util.List;

import ru.xfit.BR;
import ru.xfit.R;
import ru.xfit.databinding.LayoutTrainersBinding;
import ru.xfit.domain.App;
import ru.xfit.misc.adapters.BaseAdapter;
import ru.xfit.misc.adapters.BaseVM;
import ru.xfit.misc.adapters.FilterableAdapter;
import ru.xfit.misc.adapters.OnCancelSearchListener;
import ru.xfit.model.data.club.ClubItem;
import ru.xfit.model.data.schedule.Trainer;
import ru.xfit.model.service.Api;
import ru.xfit.screens.BlankToolbarController;
import ru.xfit.screens.XFitController;

/**
 * Created by TESLA on 15.11.2017.
 */

public class TrainersController extends BlankToolbarController<LayoutTrainersBinding>
        implements SearchView.OnQueryTextListener, OnCancelSearchListener {

    @Bindable
    public final FilterableAdapter<TrainerVM> adapter = new FilterableAdapter<>(new ArrayList<>());
    public final ObservableBoolean progress = new ObservableBoolean();

    private final TrainerFilter trainerFilter = new TrainerFilter("");

    private final ClubItem club;

    public TrainersController(ClubItem club) {
        this.club = club;

        adapter.addFilter(trainerFilter);
        progress.set(true);
        Request.with(this, Api.class)
                .create(api -> api.getTrainers(club.id))
                .onFinally(() -> progress.set(false))
                .execute(this::addTrainers);
    }

    private void addTrainers(List<Trainer> trainers) {
        List<TrainerVM> toAdd = new ArrayList<>();
        for (Trainer trainer : trainers) {
            toAdd.add(new TrainerVM(trainer, this));
        }
        adapter.addAll(toAdd);

        //TODO why not working without this shit
        notifyPropertyChanged(BR.adapter);
        notifyPropertyChanged(BR.trainersEmpty);
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_trainers;
    }

    @Override
    public String getTitle() {
        return App.getContext().getString(R.string.trainers_fitness_trainers);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        trainerFilter.setTrainerName(query);
        adapter.refresh();

        //TODO why not working without this shit
        notifyPropertyChanged(BR.adapter);
        notifyPropertyChanged(BR.trainersEmpty);
        return false;
    }

    @Bindable
    public boolean isTrainersEmpty() {
        return adapter.getItemCount() == 0;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    @Override
    public void onNavigationClick() {
        back();
    }

    @Override
    public void onCancel() {
        trainerFilter.setTrainerName("");
        adapter.refresh();

        //TODO why not working without this shit
        notifyPropertyChanged(BR.adapter);
        notifyPropertyChanged(BR.trainersEmpty);
    }
}
