package ru.xfit.screens.schedule;

import android.view.View;

import com.controllers.Request;

import ru.xfit.R;
import ru.xfit.databinding.LayoutClassBinding;
import ru.xfit.model.data.schedule.Schedule;
import ru.xfit.model.service.Api;
import ru.xfit.screens.XFitController;

/**
 * Created by TESLA on 05.11.2017.
 */

public class ClassController extends XFitController<LayoutClassBinding> {

    public Schedule schedule;
    public boolean isAdded = false;

    public ClassController(Schedule schedule) {
        this.schedule = schedule;
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
                    .create(api -> api.deleteClass(schedule.id))
                    .execute(deleteClassResponse -> {
                        isAdded = false;
                    });
        }
    }
}
