package ru.xfit.screens.settings;

import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.net.Uri;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TimePicker;

import com.controllers.Request;

import org.joda.time.DateTime;

import ru.xfit.R;
import ru.xfit.databinding.LayoutSettingsBinding;
import ru.xfit.domain.App;
import ru.xfit.model.data.notification.NotificationSettings;
import ru.xfit.model.service.Api;
import ru.xfit.screens.DrawerController;

/**
 * Created by TESLA on 28.11.2017.
 */

public class SettingsController extends DrawerController<LayoutSettingsBinding> {

    public final ObservableBoolean isNotify = new ObservableBoolean(true);
    public final ObservableField<String> dayNotify = new ObservableField<>();
    private final ObservableInt day = new ObservableInt();
    public final ObservableField<String> timeNotify = new ObservableField<>();

    public SettingsController() {
        //get setting from storage
        getSettings();
    }

    public void saveSettings() {
        NotificationSettings settings = new NotificationSettings();
        settings.isNotify = isNotify.get();
        settings.notifyTime = timeNotify.get();
        settings.notifyDay = day.get();

        Request.with(this, Api.class)
                .create(api -> api.saveNotifySettings(settings))
                .execute();
    }

    public void getSettings() {
        Request.with(this, Api.class)
                .create(Api::getNotifySettings)
                .execute(settings -> {
                    isNotify.set(settings.isNotify);
                    timeNotify.set(settings.notifyTime);
                    dayNotify.set(App.getContext()
                            .getResources()
                            .getQuantityString(R.plurals.settings_notify_day, settings.notifyDay, settings.notifyDay));
                });
    }

    @Override
    public String getTitle() {
        return App.getContext().getResources().getString(R.string.settings_title);
    }

    public void onSetDay(View view) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.inflate(R.menu.menu_notify_day);
        popupMenu.setOnMenuItemClickListener(item -> {
            int selectedDay = -1;
            switch (item.getItemId()) {
                case R.id.notify_zero:
                    selectedDay = 0;
                    break;
                case R.id.notify_one:
                    selectedDay = 1;
                    break;
                default:
                    break;
            }
            if (selectedDay > -1) {
                day.set(selectedDay);
                dayNotify.set(App.getContext()
                        .getResources()
                        .getQuantityString(R.plurals.settings_notify_day, selectedDay, selectedDay));
                return true;
            }
            return false;
        });

        popupMenu.show();
    }

    public void onSetNotify(View view) {
        saveSettings();
    }

    public void onSetTimeClick(View view) {
        if (timeCallBack != null) {
            TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), timeCallBack, 10, 0, true);
            timePickerDialog.show();
        }
    }

    public void onRateClick(View view) {
        Uri uri = Uri.parse("market://details?id=" + App.getContext().getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            getActivity().startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            getActivity().startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + App.getContext().getPackageName())));
        }
    }

    public void onAboutClick(View view) {
        show(new AboutController());
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_settings;
    }

    private transient TimePickerDialog.OnTimeSetListener timeCallBack = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            DateTime dateTime = new DateTime(DateTime.now().withHourOfDay(hourOfDay).withMinuteOfHour(minute));
            timeNotify.set(dateTime.toString("HH:mm"));

            SettingsController.this.saveSettings();
        }
    };
}
