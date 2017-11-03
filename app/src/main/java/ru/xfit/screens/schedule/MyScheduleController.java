package ru.xfit.screens.schedule;

import android.databinding.ObservableField;
import android.view.View;

import ru.xfit.R;
import ru.xfit.databinding.LayoutMyScheduleBinding;
import ru.xfit.misc.OnViewReadyListener;
import ru.xfit.screens.XFitController;

/**
 * Created by TESLA on 25.10.2017.
 */

public class MyScheduleController extends XFitController<LayoutMyScheduleBinding>  implements OnViewReadyListener {

    ObservableField<String> year = new ObservableField<>();
    ObservableField<String> week = new ObservableField<>();

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
