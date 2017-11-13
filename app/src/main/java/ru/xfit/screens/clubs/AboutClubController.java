package ru.xfit.screens.clubs;

import android.view.View;

import ru.xfit.R;
import ru.xfit.databinding.LayoutAboutClubBinding;
import ru.xfit.model.data.club.ClubItem;
import ru.xfit.screens.BlankToolbarController;
import ru.xfit.screens.XFitController;

/**
 * Created by TESLA on 13.11.2017.
 */

public class AboutClubController extends BlankToolbarController<LayoutAboutClubBinding> {
    public ClubItem club;


    public AboutClubController(ClubItem club) {
        this.club = club;
    }

    public void back(View view) {
        back();
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_about_club;
    }
}
