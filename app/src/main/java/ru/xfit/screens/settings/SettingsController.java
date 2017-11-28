package ru.xfit.screens.settings;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.view.View;

import ru.xfit.R;
import ru.xfit.databinding.LayoutSettingsBinding;
import ru.xfit.domain.App;
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

    @Override
    public String getTitle() {
        return App.getContext().getResources().getString(R.string.settings_title);
    }

    public void onSetDay(View view) {

    }

    public void onSetTimeClick(View view) {

    }

    public void onRateClick(View view) {

    }

    public void onAboutClick(View view) {
        show(new AboutController());
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_settings;
    }
}
