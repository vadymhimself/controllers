package ru.xfit.screens.auth;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.view.View;

import ru.xfit.R;
import ru.xfit.databinding.LayoutRegisterBinding;
import ru.xfit.screens.XFitController;

/**
 * Created by TESLA on 25.10.2017.
 */

public class RegisterController extends XFitController<LayoutRegisterBinding>{
    public ObservableField<String> phone = new ObservableField<>("");
    public ObservableField<String> date = new ObservableField<>("");
    public ObservableField<String> password = new ObservableField<>("");
    public ObservableField<String> passwordRepeat = new ObservableField<>("");
    public ObservableField<String> name = new ObservableField<>("");
    public ObservableField<String> email = new ObservableField<>("");

    public ObservableBoolean phoneFocus = new ObservableBoolean(false);
    public ObservableBoolean nameFocus = new ObservableBoolean(false);
    public ObservableBoolean emailFocus = new ObservableBoolean(false);
    public ObservableBoolean passwordFocus = new ObservableBoolean(false);
    public ObservableBoolean passwordRepFocus = new ObservableBoolean(false);
    public ObservableBoolean dateFocus = new ObservableBoolean(false);

    @Override
    public int getLayoutId() {
        return R.layout.layout_register;
    }

    public void back(View view) {
        back();
    }

    public void register(View view) {

    }
}
