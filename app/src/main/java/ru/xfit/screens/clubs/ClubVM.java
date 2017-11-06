package ru.xfit.screens.clubs;

import android.view.View;

import ru.xfit.R;
import ru.xfit.misc.adapters.BaseVM;
import ru.xfit.model.data.schedule.ScheduleClub;

/**
 * Created by TESLA on 06.11.2017.
 */

public class ClubVM implements BaseVM {

    public ScheduleClub scheduleClub;
    public ClubsController clubsController;

    public ClubVM(ScheduleClub scheduleClub, ClubsController clubsController) {
        this.scheduleClub = scheduleClub;
        this.clubsController = clubsController;
    }

    public void onItemClick(View view){

    }

    @Override
    public int getLayoutId() {
        return R.layout.item_club;
    }
}
