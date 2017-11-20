package ru.xfit.screens.clubs;

import android.databinding.ObservableBoolean;
import android.view.View;

import okhttp3.Request;
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
    private boolean fromMyXfit;

    public ClubVM(ClubItem club, ClubsController clubsController, boolean isMyClub, boolean fromMyXfit) {
        this.club = club;
        this.clubsController = clubsController;
        this.isMyClub.set(isMyClub);
        this.fromMyXfit = fromMyXfit;
    }

    public void onItemClick(View view){
        if (fromMyXfit) {
            clubsController.linkToClub(club.id);
        } else {
            clubsController.show(new AboutClubController(club, isMyClub.get()));
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_club;
    }
}
