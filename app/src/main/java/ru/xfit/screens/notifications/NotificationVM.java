package ru.xfit.screens.notifications;

import android.view.View;

import ru.xfit.R;
import ru.xfit.misc.adapters.BaseVM;

/**
 * Created by TESLA on 29.11.2017.
 */

public class NotificationVM  implements BaseVM {
    public final String time;
    public final String text;
    public NotificationsController controller;

    public NotificationVM(NotificationsController controller, String time, String text) {
        this.time = time;
        this.text = text;
        this.controller = controller;
    }

    public void onItemClick(View view) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.item_notification;
    }
}
