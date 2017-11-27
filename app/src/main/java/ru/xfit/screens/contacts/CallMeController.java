package ru.xfit.screens.contacts;

import android.databinding.ObservableField;
import android.view.View;

import ru.xfit.R;
import ru.xfit.databinding.LayoutCallMeBinding;
import ru.xfit.domain.App;
import ru.xfit.screens.XFitController;

/**
 * Created by TESLA on 27.11.2017.
 */

public class CallMeController extends XFitController<LayoutCallMeBinding> {

    public ObservableField<String> phone = new android.databinding.ObservableField<>("");

    @Override
    public int getLayoutId() {
        return R.layout.layout_call_me;
    }

    @Override
    public String getTitle() {
        return App.getContext().getResources().getString(R.string.call_me_title);
    }

    public void callMe(View view) {

    }
}
