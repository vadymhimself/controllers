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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.controllers.Controller;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hwangjr.rxbus.Bus;
import com.hwangjr.rxbus.annotation.Subscribe;

import javax.inject.Inject;

import io.fabric.sdk.android.Fabric;
import ru.xfit.BuildConfig;
import ru.xfit.R;
import ru.xfit.misc.TransparentStatusBar;
import ru.xfit.misc.events.OnBackEvent;
import ru.xfit.misc.events.OptionsItemSelectedEvent;
import ru.xfit.model.data.storage.preferences.PreferencesManager;
import ru.xfit.model.retrorequest.LogoutEvent;
import ru.xfit.screens.BlankToolbarController;
import ru.xfit.screens.DrawerController;
import ru.xfit.screens.FeedbackController;
import ru.xfit.screens.clubs.ClubsController;
import ru.xfit.screens.contacts.ContactsController;
import ru.xfit.screens.notifications.NotificationsController;
import ru.xfit.screens.schedule.ClubClassesController;
import ru.xfit.screens.schedule.MyScheduleController;
import ru.xfit.screens.settings.SettingsController;
import ru.xfit.screens.xfit.MyXfitController;


public class MainActivity extends XFitActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    public MyScheduleController myScheduleController;
    @Inject
    Bus bus;
//    public ClubsController clubsController;
    private Toolbar toolbar;
    private NavigationView navView;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private View statusBarBgr;

    private TextView buildVersion;

    private boolean showFilterAndSearch;

    private boolean toolBarNavigationListenerIsRegistered;

    private ViewGroup transitionsContainer;

    public static void start(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        App.getInjector().inject(this);
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        setControllerContainer(R.id.container);

        transitionsContainer = (ViewGroup) findViewById(R.id.transitions_container);

        buildVersion = (TextView) findViewById(R.id.build_ver);
        buildVersion.setText(getVersionName());


        statusBarBgr = findViewById(R.id.statusBarBgr);


        drawer = (DrawerLayout) findViewById(R.id.drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navView = (NavigationView) findViewById(R.id.nav_view);
        navView.setItemIconTintList(null);

        setSupportActionBar(toolbar);

        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        statusBarBgr.getLayoutParams().height = getResources().getDimensionPixelSize(resourceId);
        buildVersion.setPadding(0, getResources().getDimensionPixelSize(resourceId), 0, 0);


        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        drawer.setScrimColor(getResources().getColor(android.R.color.transparent));
        drawer.setDrawerShadow(R.drawable.transparent, GravityCompat.START);
        drawer.setElevation(0f);
        toggle.syncState();
        navView.setNavigationItemSelectedListener(this);

        myScheduleController = new MyScheduleController();
//        clubsController = new ClubsController(false);
        if (savedInstanceState == null) {
            show(myScheduleController, 0, 0);
        }

        String token = FirebaseInstanceId.getInstance().getToken();
        Log.e(">>>>>>>>", "Firebase token: " + token);
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
        setVisibleStatusBar(!(controller instanceof TransparentStatusBar));

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
            case R.id.notifications:
                replace(new NotificationsController());
                drawer.closeDrawers();
                return true;
            case R.id.clubs:
                replace(new ClubsController(false));
                drawer.closeDrawers();
                return true;
            case R.id.contacts:
                replace(new ContactsController());
                drawer.closeDrawers();
                return true;
            case R.id.settings:
                replace(new SettingsController());
                drawer.closeDrawers();
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

        this.finish();
        StartActivity.start(this);
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

    public void setVisibleStatusBar(boolean visible) {
        if (statusBarBgr.getVisibility() == View.VISIBLE && !visible) {
            //transition hide
            statusBarBgr.postDelayed(new Runnable() {
                @Override
                public void run() {
                    TransitionManager.beginDelayedTransition(transitionsContainer);
                    statusBarBgr.setVisibility(View.GONE);
                }
            }, 10);

        } else if (statusBarBgr.getVisibility() == View.GONE && visible) {
            //transition show
//            TransitionManager.beginDelayedTransition(transitionsContainer);
            statusBarBgr.setVisibility(View.VISIBLE);
        } else {
            //nothing
        }
    }

    public String getVersionName() {
        return "build: " + BuildConfig.VERSION_NAME;
    }

    @Override
    protected Controller back() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        if(getCurrentFocus() != null)
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);

        return super.back();
    }

    @Override
    protected boolean beforeControllersChanged(Controller previous, Controller next) {
        //TODO remove this shit
        if (previous instanceof FeedbackController) {
            if (((FeedbackController) previous).prevPopup.get() != null) {
                ((PopupWindow)((FeedbackController) previous).prevPopup.get()).dismiss();
            }
        }
//        if(previous instanceof AboutNewsController)
//            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        if(next instanceof AboutNewsController)
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        return super.beforeControllersChanged(previous, next);
    }


    @Override
    public void onBackPressed() {
        //TODO why event not received ?
        App.getBus().post(new OnBackEvent());
        super.onBackPressed();
    }
}
