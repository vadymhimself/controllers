package ru.xfit.screens.schedule;

import android.view.View;
import com.controllers.Request;
import ru.xfit.R;
import ru.xfit.databinding.LayoutClassBinding;
import ru.xfit.misc.OnViewReadyListener;
import ru.xfit.model.data.schedule.Clazz;
import ru.xfit.model.service.Api;
import ru.xfit.screens.XFitController;

/**
 * Created by TESLA on 05.11.2017.
 */

public class ClassController extends XFitController<LayoutClassBinding> implements OnViewReadyListener {

    public Clazz schedule;
    public boolean isAdded = false;
    private boolean isCanDelete = false;

    public ClassController(Clazz schedule, boolean isCanDelete) {
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
                        updateButtonText("Удалить из расписания");
                    });
        } else {
            Request.with(this, Api.class)
                    .create(api -> api.deleteClass(schedule.subscriptionId))
                    .execute(deleteClassResponse -> {
                        isAdded = false;
                        updateButtonText("Добавить в расписание");
                    });
        }
    }

    private void updateButtonText(String text) {
        if (getBinding() == null)
            return;

        getBinding().subscribeBtn.setText(text);
    }

    @Override
    public void onReady(View root) {
        if (getBinding() == null)
            return;

        if (isCanDelete)
            updateButtonText("Удалить из расписания");
        else
            updateButtonText("Добавить в расписание");
    }
}
