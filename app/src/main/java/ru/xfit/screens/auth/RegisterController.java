package ru.xfit.screens.auth;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.controllers.Request;

import ru.xfit.R;
import ru.xfit.databinding.LayoutRegisterBinding;
import ru.xfit.misc.NavigationClickListener;
import ru.xfit.model.data.register.RegisterRequest;
import ru.xfit.model.service.Api;
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
    public ObservableInt gender = new ObservableInt();
    public ObservableField<String> errorResponse = new ObservableField<>();

    private boolean isValidate = false;
    public ObservableBoolean isPasswordValid = new ObservableBoolean();
    public ObservableBoolean isRePasswordValid = new ObservableBoolean();
    public ObservableBoolean isEmailValid = new ObservableBoolean();
    public ObservableBoolean isTelValid = new ObservableBoolean();
    public ObservableBoolean isNameValid = new ObservableBoolean();
    public ObservableBoolean isDateValid = new ObservableBoolean();

    public ObservableBoolean progress = new ObservableBoolean();

    @Override
    public int getLayoutId() {
        return R.layout.layout_register;
    }

    public void back(View view) {
        back();
    }

    public void register(View view) {

        if (isNameValid.get() && isTelValid.get() && isEmailValid.get()
                && isRePasswordValid.get() && isPasswordValid.get() && isDateValid.get()) {
            isValidate = true;
        }

        if (isValidate) {
            progress.set(true);
            RegisterRequest regData = new RegisterRequest();
            regData.phone = phone.get();
            //01234567
            String bDay = date.get().substring(4,8) + "-" + date.get().substring(0,2) + "-" + date.get().substring(2,4);
            regData.birthday = bDay;
            regData.password = password.get();
            regData.name = name.get();
            regData.email = email.get();
            if (gender.get() == R.id.male)
                regData.gender = "male";
            else
                regData.gender = "female";

            Request.with(this, Api.class)
                    .create(api -> api.pleaseConfirm(regData.phone))
                    .onError(error -> {
                        errorResponse.set(error.getMessage());
                        Snackbar.make(view, "Error: " + error.getMessage(), BaseTransientBottomBar.LENGTH_LONG).show();
                    })
                    .onFinally(() -> progress.set(false))
                    .execute(confirmationResponse -> {
                        if (confirmationResponse.sent) {
                            show(new SmsConfirmController(regData));
                        } else {
                            Snackbar.make(view, "Следующая попытка: " + confirmationResponse.nextAttempt, BaseTransientBottomBar.LENGTH_LONG).show();
                        }
                    });
        }
    }
}
