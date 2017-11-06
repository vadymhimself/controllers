package ru.xfit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import com.controllers.ControllerActivity;
import ru.xfit.screens.BusTestController;
import ru.xfit.screens.HomeController;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import ru.xfit.screens.clubs.ClubsController;
import ru.xfit.screens.schedule.MyScheduleController;

public class MainActivity extends XFitActivity implements
        NavigationView.OnNavigationItemSelectedListener {
    public static void start(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public MyScheduleController myScheduleController;
    public ClubsController clubsController;

    private Toolbar toolbar;
    private NavigationView navView;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;

    private boolean toolBarNavigationListenerIsRegistered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        setControllerContainer(R.id.container);

        drawer = (DrawerLayout) findViewById(R.id.drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navView = (NavigationView) findViewById(R.id.nav_view);
        navView.setItemIconTintList(null);

        setSupportActionBar(toolbar);

        myScheduleController = new MyScheduleController();
        clubsController = new ClubsController();
        if (savedInstanceState == null) {
            show(myScheduleController, 0, 0);
        }

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navView.setNavigationItemSelectedListener(this);

        setTitle(getResources().getString(R.string.my_schedule_title));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.my_schedule:
                replace(myScheduleController);
                setTitle(getResources().getString(R.string.my_schedule_title));
                drawer.closeDrawers();
                return true;
            case R.id.my_xfit:
                return true;
            case R.id.services:
                return true;
            case R.id.clubs:
                replace(clubsController);
                setTitle(getResources().getString(R.string.clubs_title));
                drawer.closeDrawers();
                return true;
            case R.id.contacts:
                return true;
            case R.id.settings:
                return true;
            case R.id.quit:
                return true;
        }
        return true;
    }

    public void setTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    public void showHamburgerIcon(boolean hide) {
        if(hide) {
            toggle.setDrawerIndicatorEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            if(!toolBarNavigationListenerIsRegistered) {
                toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });

                toolBarNavigationListenerIsRegistered = true;
            }

        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            toggle.setDrawerIndicatorEnabled(true);
            toggle.setToolbarNavigationClickListener(null);
            toolBarNavigationListenerIsRegistered = false;
        }
    }
}
