package ru.xfit.screens.schedule;

import android.view.View;

import com.controllers.Request;

import ru.xfit.R;
import ru.xfit.databinding.LayoutClassBinding;
import ru.xfit.misc.OnViewReadyListener;
import ru.xfit.model.data.schedule.Schedule;
import ru.xfit.model.service.Api;
import ru.xfit.screens.XFitController;

/**
 * Created by TESLA on 05.11.2017.
 */

public class ClassController extends XFitController<LayoutClassBinding> implements OnViewReadyListener {

    public Schedule schedule;
    public boolean isAdded = false;
    private boolean isCanDelete = false;

    public ClassController(Schedule schedule, boolean isCanDelete) {
        this.schedule = schedule;
        this.isCanDelete = isCanDelete;

        if (isCanDelete) {
            isAdded = true;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_class;
    }

    public void subscribeClass(View view) {
        Request.setDefaultErrorAction(Throwable::printStackTrace);
        if (!isAdded) {
            Request.with(this, Api.class)
                    .create(api -> api.addClass(schedule.id))
                    .execute(addClassResponse -> {
                        isAdded = true;
                    });
            getBinding().subscribeBtn.setText("Удалить из расписания");
        } else {
            Request.with(this, Api.class)
                    .create(api -> api.deleteClass(schedule.subscriptionId))
                    .execute(deleteClassResponse -> {
                        isAdded = false;
                    });
            getBinding().subscribeBtn.setText("Добавить в расписание");
        }
    }

    @Override
    public void onReady(View root) {
        if (getBinding() == null)
            return;

        if (isCanDelete)
            getBinding().subscribeBtn.setText("Удалить из расписания");
        else
            getBinding().subscribeBtn.setText("Добавить в расписание");
    }
}
