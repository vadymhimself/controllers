package ru.xfit.screens.clubs;

import android.content.Intent;
import android.net.Uri;
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
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_about_trainer;
    }

    @Override
    public String getTitle() {
        return trainer.title;
    }

    public void getTrainerFacebook(View view) {
        Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse(trainer.socialLinks.facebookLink));
        getActivity().startActivity(intent);
    }

    public void getTrainerInsta(View view) {
        Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse(trainer.socialLinks.instagramLink));
        getActivity().startActivity(intent);
    }

    public void getTrainerVk(View view) {
        Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse(trainer.socialLinks.vkLink));
        getActivity().startActivity(intent);
    }

    @Override
    public void onNavigationClick() {
        back();
    }
}
