package ru.xfit.screens.clubs;

import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.view.View;

import ru.xfit.R;
import ru.xfit.databinding.LayoutAboutTrainerBinding;
import ru.xfit.misc.NavigationClickListener;
import ru.xfit.model.data.schedule.Trainer;
import ru.xfit.screens.BlankToolbarController;
import ru.xfit.screens.XFitController;

/**
 * Created by TESLA on 15.11.2017.
 */

public class AboutTrainerController extends BlankToolbarController<LayoutAboutTrainerBinding> implements NavigationClickListener{
    public Trainer trainer;

    public AboutTrainerController(Trainer trainer) {
        this.trainer = trainer;
        setTitle(trainer.title);
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_about_trainer;
    }

    public void getTrainerFacebook(View view) {
        Snackbar.make(view, "Coming soon...", BaseTransientBottomBar.LENGTH_SHORT).show();
    }

    public void getTrainerInsta(View view) {
        Snackbar.make(view, "Coming soon...", BaseTransientBottomBar.LENGTH_SHORT).show();
    }

    public void getTrainerYoutube(View view) {
        Snackbar.make(view, "Coming soon...", BaseTransientBottomBar.LENGTH_SHORT).show();
    }

    @Override
    public void onNavigationClick() {
        back();
    }
}
