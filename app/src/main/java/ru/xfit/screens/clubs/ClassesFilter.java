package ru.xfit.screens.clubs;

import java.io.Serializable;

import ru.xfit.misc.adapters.PredicateFilter;
import ru.xfit.screens.schedule.ClassVM;

/**
 * Created by TESLA on 27.11.2017.
 */

public class ClassesFilter extends PredicateFilter<ClassVM> implements Serializable {
    private String clubSearch;

    public ClassesFilter(String clubSearch) {
        this.clubSearch = clubSearch;
    }

    public void setTrainerName(String clubSearch) {
        this.clubSearch = clubSearch;
    }

    @Override
    protected boolean call(ClassVM clubVM) {
        return clubSearch.isEmpty()
                || clubVM.clazz.room.title.toLowerCase().contains(clubSearch.toLowerCase())
                || checkTrainer(clubVM)
                || clubVM.clazz.activity.title.toLowerCase().contains(clubSearch.toLowerCase());
    }

    private boolean checkTrainer(ClassVM classVM) {
        for (int i = 0; i < classVM.clazz.trainers.size(); i++)
            if (classVM.clazz.trainers.get(i).title.toLowerCase().contains(clubSearch.toLowerCase()))
                return true;

        return false;
    }
}
