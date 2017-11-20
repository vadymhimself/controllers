package ru.xfit.domain;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.controllers.Controller;
import com.crashlytics.android.Crashlytics;
import com.hwangjr.rxbus.Bus;
import com.hwangjr.rxbus.annotation.Subscribe;
import io.fabric.sdk.android.Fabric;
import ru.xfit.R;
import ru.xfit.misc.events.OptionsItemSelectedEvent;
import ru.xfit.model.data.storage.preferences.PreferencesManager;
import ru.xfit.model.retrorequest.LogoutEvent;
import ru.xfit.screens.BlankToolbarController;
import ru.xfit.screens.DrawerController;
import ru.xfit.screens.clubs.ClubsController;
import ru.xfit.screens.clubs.SuspendCardController;
import ru.xfit.screens.schedule.ClubClassesController;
import ru.xfit.screens.schedule.MyScheduleController;
import ru.xfit.screens.xfit.MyXfitController;

import javax.inject.Inject;


public class MainActivity extends XFitActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    @Inject Bus bus;

    public MyScheduleController myScheduleController;
    public ClubsController clubsController;

    private Toolbar toolbar;
    private NavigationView navView;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;

    private boolean showFilterAndSearch;

    private boolean toolBarNavigationListenerIsRegistered;

    private ViewGroup transitionsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        App.getInjector().inject(this);
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        setControllerContainer(R.id.container);

        transitionsContainer = (ViewGroup) findViewById(R.id.transitions_container);

        drawer = (DrawerLayout) findViewById(R.id.drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navView = (NavigationView) findViewById(R.id.nav_view);
        navView.setItemIconTintList(null);

        setSupportActionBar(toolbar);

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        drawer.setScrimColor(getResources().getColor(android.R.color.transparent));
        drawer.setDrawerShadow(R.drawable.transparent, GravityCompat.START);
        drawer.setElevation(0f);
        toggle.syncState();
        navView.setNavigationItemSelectedListener(this);

        myScheduleController = new MyScheduleController();
        clubsController = new ClubsController(false);
        if (savedInstanceState == null) {
            show(myScheduleController, 0, 0);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        bus.register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        bus.unregister(this);
    }

    @Override
    protected void onControllerChanged(Controller controller) {

        setVisibleHamburgerIcon(!(controller instanceof DrawerController));
        setVisibleToolbar(!(controller instanceof BlankToolbarController));

        if (controller instanceof ClubClassesController) {
            // TODO: push to the top controller or make a menu presenter
            showFilterAndSearch = true;
            invalidateOptionsMenu();
        } else {
            showFilterAndSearch = false;
            invalidateOptionsMenu();
        }

        setTitle(controller.getTitle());

        super.onControllerChanged(controller);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_schedules, menu);
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.search).setVisible(showFilterAndSearch);
        menu.findItem(R.id.filter).setVisible(showFilterAndSearch);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        App.getBus().post(new OptionsItemSelectedEvent(item));
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.my_schedule:
                replace(myScheduleController);
                drawer.closeDrawers();
                return true;
            case R.id.my_xfit:
                replace(new MyXfitController());
                drawer.closeDrawers();
                return true;
            case R.id.services:
                return true;
            case R.id.clubs:
                replace(clubsController);
                drawer.closeDrawers();
                return true;
            case R.id.contacts:
                return true;
            case R.id.settings:
                return true;
            case R.id.quit:
                logOut();
                return true;
        }
        return true;
    }

    @Subscribe
    public void onTriggerLogout(LogoutEvent logoutEvent) {
        logOut();
    }

    public void logOut() {
        PreferencesManager preferencesManager = new PreferencesManager(App.getContext());
        preferencesManager.putValue(PreferencesManager.KEY_IS_USER_ALREADY_LOGIN, false);

        StartActivity.start(this);
        finishAffinity();
    }

    public void setTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    public void setVisibleHamburgerIcon(boolean visible) {
        if (visible) {
            toggle.setDrawerIndicatorEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            if (!toolBarNavigationListenerIsRegistered) {
                toggle.setToolbarNavigationClickListener(v -> onBackPressed());

                toolBarNavigationListenerIsRegistered = true;
            }

        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            toggle.setDrawerIndicatorEnabled(true);
            toggle.setToolbarNavigationClickListener(null);
            toolBarNavigationListenerIsRegistered = false;
        }
    }

    public void setVisibleToolbar(boolean visible) {

        if (toolbar.getVisibility() == View.VISIBLE && !visible) {
            //transition hide
            toolbar.postDelayed(new Runnable() {
                @Override
                public void run() {
                    TransitionManager.beginDelayedTransition(transitionsContainer);
                    toolbar.setVisibility(View.GONE);
                }
            }, 10);

        } else if (toolbar.getVisibility() == View.GONE && visible) {
            //transition show
            TransitionManager.beginDelayedTransition(transitionsContainer);
            toolbar.setVisibility(View.VISIBLE);
        } else {
            //nothing
        }
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }
}
