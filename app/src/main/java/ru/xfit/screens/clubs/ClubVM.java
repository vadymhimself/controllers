package ru.xfit.screens.clubs;

import android.databinding.ObservableBoolean;
import android.view.View;
import ru.xfit.R;
import ru.xfit.misc.adapters.BaseVM;
import ru.xfit.model.data.club.Club;
import ru.xfit.model.data.club.ClubItem;
import ru.xfit.model.data.schedule.Schedule;

/**
 * Created by TESLA on 06.11.2017.
 */

public class ClubVM implements BaseVM {

    public ClubItem club;
    public ClubsController clubsController;
    public ObservableBoolean isMyClub = new ObservableBoolean();

    public ClubVM(ClubItem club, ClubsController clubsController) {
        this.club = club;
        this.clubsController = clubsController;
    }

    public void onItemClick(View view){
        clubsController.show(new AboutClubController(club));
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_club;
    }
}
