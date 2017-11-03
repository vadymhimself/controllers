package ru.xfit.screens.schedule;

import android.databinding.ObservableField;
import android.util.Log;
import android.view.View;

import com.controllers.Request;

import ru.xfit.R;
import ru.xfit.databinding.LayoutMyScheduleBinding;
import ru.xfit.misc.OnViewReadyListener;
import ru.xfit.model.service.Api;
import ru.xfit.screens.XFitController;

/**
 * Created by TESLA on 25.10.2017.
 */

public class MyScheduleController extends XFitController<LayoutMyScheduleBinding>  implements OnViewReadyListener {

    public ObservableField<String> year = new ObservableField<>();
    public ObservableField<String> week = new ObservableField<>();

    public MyScheduleController() {
        Request.with(this, Api.class)
                .create(api -> api.getMySchedule(year.get(), week.get()))
                .onError(error -> {
//                    errorResponse.set(error.getMessage());
//                    Snackbar.make(view, "Ошибка: " + error.getMessage(), BaseTransientBottomBar.LENGTH_LONG).show();
                })
                .execute(scheduleListResponse -> {
                    Log.d(">>>>", "" + scheduleListResponse.dateSince);
                });

    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_my_schedule;
    }

    @Override
    public void onReady(View root) {
        if (getBinding() == null)
            return;
    }
}
