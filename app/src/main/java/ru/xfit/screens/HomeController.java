package ru.xfit.screens;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.controllers.Promise;
import com.controllers.Request;
import com.hwangjr.rxbus.annotation.Subscribe;
import ru.xfit.R;
import ru.xfit.databinding.LayoutHomeBinding;
import ru.xfit.domain.App;
import ru.xfit.misc.OnViewReadyListener;
import ru.xfit.model.service.Api;
import ru.xfit.screens.auth.AuthController;

public class HomeController extends XFitController<LayoutHomeBinding> implements OnViewReadyListener,
        NavigationView.OnNavigationItemSelectedListener {

    public HomeController() {

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

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), getBinding().drawer, getBinding().toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        getBinding().drawer.addDrawerListener(toggle);
        toggle.syncState();
        getBinding().navView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
