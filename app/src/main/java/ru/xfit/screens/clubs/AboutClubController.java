package ru.xfit.screens.clubs;

import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.net.Uri;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.controllers.Request;

import ru.xfit.R;
import ru.xfit.databinding.LayoutAboutClubBinding;
import ru.xfit.misc.NavigationClickListener;
import ru.xfit.model.data.club.ClubItem;
import ru.xfit.model.service.Api;
import ru.xfit.screens.BlankToolbarController;
import ru.xfit.screens.XFitController;
import ru.xfit.screens.schedule.ClubClassesController;

/**
 * Created by TESLA on 13.11.2017.
 */

public class AboutClubController extends BlankToolbarController<LayoutAboutClubBinding> implements NavigationClickListener {
    public ClubItem club;

    public final ObservableBoolean progress = new ObservableBoolean();
    public ObservableBoolean isMyClub = new ObservableBoolean();

    public AboutClubController(ClubItem club, boolean isMyClub) {
        this.club = club;
        this.isMyClub.set(isMyClub);
        setTitle(club.title);
    }

    public void back(View view) {
        back();
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_about_club;
    }

    public String getAddress() {
        if (club.city.startsWith("!")) {
            return club.city.substring(1, club.city.length()) + " " + club.address;
        } else
            return club.city + " " + club.address;
    }

    public void getRoute(View view) {
        if (club.latitude == null || club.longitude == null)
            return;
        Uri gmmIntentUri = Uri.parse(String.format("google.navigation:q=%s,%s", club.latitude, club.longitude));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(view.getContext().getPackageManager()) != null) {
            if (getActivity() != null)
                getActivity().startActivity(mapIntent);
        }
    }

    public void getCall(View view) {
        if (club.phone == null)
            return;

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + club.phone));
        if (getActivity() != null)
            getActivity().startActivity(intent);
    }

    public void sendEmail(View view) {
        if (club.email == null)
            return;

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto",club.email, null));
        if (getActivity() != null)
            getActivity().startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

    public void getSchedule(View view) {
        progress.set(true);
        Request.with(this, Api.class)
                .create(api -> api.getClassesForClub(club.id))
                .onFinally(() -> progress.set(false))
                .execute(schedule -> {
                    AboutClubController.this.show(new ClubClassesController(schedule));
                });

    }

    public void suspendCard(View view) {
        Snackbar.make(view, "Coming soon...", BaseTransientBottomBar.LENGTH_SHORT).show();
    }

    public void getTrainers(View view) {
        show(new TrainersController(club));
    }

    public void getNews(View view) {
        Snackbar.make(view, "Coming soon...", BaseTransientBottomBar.LENGTH_SHORT).show();
    }

    public void getGuestVisit(View view) {
        Snackbar.make(view, "Coming soon...", BaseTransientBottomBar.LENGTH_SHORT).show();
    }

    public void buyCard(View view) {
        Snackbar.make(view, "Coming soon...", BaseTransientBottomBar.LENGTH_SHORT).show();
    }

    @Override
    public void onNavigationClick() {
        back();
    }
}
