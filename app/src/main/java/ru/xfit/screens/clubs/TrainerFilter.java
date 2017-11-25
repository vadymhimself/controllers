package ru.xfit.screens.clubs;

import java.io.Serializable;
import java.util.List;

import ru.xfit.misc.adapters.BaseVM;
import ru.xfit.misc.adapters.Filter;
import ru.xfit.misc.adapters.PredicateFilter;

/**
 * Created by TESLA on 25.11.2017.
 */

public class TrainerFilter extends PredicateFilter<TrainerVM> implements Serializable {

    private String trainerName;

    public TrainerFilter(String trainerName) {
        this.trainerName = trainerName;
    }

    public void setTrainerName(String trainerName) {
        this.trainerName = trainerName;
    }

    @Override
    protected boolean call(TrainerVM trainerVM) {
        if (trainerName.isEmpty())
            return true;
        else
            return trainerVM.trainer.title.toLowerCase().contains(trainerName.toLowerCase());
    }

}
