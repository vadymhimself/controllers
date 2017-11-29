package ru.xfit.screens.notifications;

import android.databinding.ObservableBoolean;

import com.controllers.Request;

import java.util.ArrayList;
import java.util.List;

import ru.xfit.R;
import ru.xfit.databinding.LayoutNotificationsBinding;
import ru.xfit.domain.App;
import ru.xfit.misc.adapters.BaseAdapter;
import ru.xfit.misc.adapters.BaseVM;
import ru.xfit.model.data.notifications.Notification;
import ru.xfit.model.service.Api;
import ru.xfit.screens.DrawerController;

/**
 * Created by TESLA on 29.11.2017.
 */

public class NotificationsController extends DrawerController<LayoutNotificationsBinding> {
    public ObservableBoolean notifysEmpty = new ObservableBoolean();
    public ObservableBoolean progress = new ObservableBoolean();
    public BaseAdapter<BaseVM> adapter = new BaseAdapter<>(new ArrayList<>());

    public NotificationsController() {
        progress.set(true);
        Request.with(this, Api.class)
                .create(Api::getNotifications)
                .onFinally(() -> progress.set(false))
                .execute(this::addMessages);
    }

    private void addMessages(List<Notification> notifications) {
        List<BaseVM> toAdd = new ArrayList<>();
        for (Notification message : notifications) {
            toAdd.add(new NotificationVM(this, message.publishDate, message.message));
        }
        adapter.addAll(toAdd);
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_notifications;
    }

    @Override
    public String getTitle() {
        return App.getContext().getResources().getString(R.string.notifications_title);
    }
}
