package ru.xfit.screens.settings;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.view.View;

import ru.xfit.R;
import ru.xfit.databinding.LayoutSettingsBinding;
import ru.xfit.screens.DrawerController;

/**
 * Created by TESLA on 28.11.2017.
 */

public class SettingsController extends DrawerController<LayoutSettingsBinding> {

    public final ObservableBoolean isNotify = new ObservableBoolean(true);
    public final ObservableField<String> dayNotify = new ObservableField<>();
    public final ObservableField<String> timeNotify = new ObservableField<>();

    public SettingsController() {
        //get setting from storage
    }

    public void onSetDay(View view) {

    }

    public void onSetTimeClick(View view) {

    }

    public void onRateClick(View view) {

    }

    public void onAboutClick(View view) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_settings;
    }
}
