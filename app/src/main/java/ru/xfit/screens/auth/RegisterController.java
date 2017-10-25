package ru.xfit.screens.auth;

import android.databinding.ObservableField;
import android.view.View;

import ru.xfit.R;
import ru.xfit.databinding.LayoutRegisterBinding;
import ru.xfit.screens.XFitController;

/**
 * Created by TESLA on 25.10.2017.
 */

public class RegisterController extends XFitController<LayoutRegisterBinding> {
    public ObservableField<String> phone = new ObservableField<>("");
    public ObservableField<String> password = new ObservableField<>("");

    @Override
    public int getLayoutId() {
        return R.layout.layout_register;
    }

    public void register(View view) {

    }
}
