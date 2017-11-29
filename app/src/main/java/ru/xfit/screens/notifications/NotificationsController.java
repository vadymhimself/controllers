package ru.xfit.screens.notifications;

import android.databinding.ObservableBoolean;

import java.util.ArrayList;

import ru.xfit.R;
import ru.xfit.databinding.LayoutNotificationsBinding;
import ru.xfit.domain.App;
import ru.xfit.misc.adapters.BaseAdapter;
import ru.xfit.misc.adapters.BaseVM;
import ru.xfit.screens.DrawerController;

/**
 * Created by TESLA on 29.11.2017.
 */

public class NotificationsController extends DrawerController<LayoutNotificationsBinding> {
    public ObservableBoolean notifysEmpty = new ObservableBoolean();
    public ObservableBoolean progress = new ObservableBoolean();
    public BaseAdapter<BaseVM> adapter = new BaseAdapter<>(new ArrayList<>());

    @Override
    public int getLayoutId() {
        return R.layout.layout_notifications;
    }

    @Override
    public String getTitle() {
        return App.getContext().getResources().getString(R.string.notifications_title);
    }
}
