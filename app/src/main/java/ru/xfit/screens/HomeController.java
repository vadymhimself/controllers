package ru.xfit.screens;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.controllers.ControllerPagerAdapter;
import com.controllers.Promise;
import com.controllers.Request;
import com.hwangjr.rxbus.annotation.Subscribe;
import ru.xfit.R;
import ru.xfit.databinding.LayoutHomeBinding;
import ru.xfit.domain.App;
import ru.xfit.misc.OnViewReadyListener;
import ru.xfit.model.service.Api;
import ru.xfit.screens.auth.AuthController;
import ru.xfit.screens.schedule.MyScheduleController;

public class HomeController extends XFitController<LayoutHomeBinding> implements OnViewReadyListener,
        NavigationView.OnNavigationItemSelectedListener {

    public ControllerPagerAdapter pagerAdapter;

    public HomeController() {
        pagerAdapter = new ControllerPagerAdapter(this);
        pagerAdapter.addController(new MyScheduleController());

    }

    public void onButtonClick() {
//        throw new RuntimeException("This is a crash");
    }

    @Subscribe
    public void onTestEvent(BusTestController.TestEvent e) {
        Toast.makeText(App.getContext(), e.name, Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_home;
    }

    @Override
    public void onReady(View root) {
        if (getBinding() == null) return;

        setTitle(root.getResources().getString(R.string.my_schedule_title));

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), getBinding().drawer, getBinding().toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        getBinding().drawer.addDrawerListener(toggle);
        toggle.syncState();
        getBinding().navView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (getBinding() == null || getActivity() == null) return false;

        switch (item.getItemId()) {
            case R.id.my_schedule:
                getBinding().pager.setCurrentItem(0);
                getBinding().drawer.closeDrawers();
                setTitle(getActivity().getResources().getString(R.string.my_schedule_title));
                return true;
            case R.id.my_xfit:
                return true;
            case R.id.services:
                return true;
            case R.id.clubs:
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
        if (getBinding() == null)
            return;

        getBinding().toolbar.setTitle(title);
    }
}
