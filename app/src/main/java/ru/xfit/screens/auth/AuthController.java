package ru.xfit.screens.auth;

import android.databinding.ObservableField;
import android.view.View;

import com.controllers.Request;

import ru.xfit.R;
import ru.xfit.databinding.LayoutAuthBinding;
import ru.xfit.model.service.Api;
import ru.xfit.screens.XFitController;
import ru.xfit.screens.schedule.MyScheduleController;

/**
 * Created by TESLA on 25.10.2017.
 */

public class AuthController extends XFitController<LayoutAuthBinding> {

    public ObservableField<String> phone = new ObservableField<>("");
    public ObservableField<String> password = new ObservableField<>("");
    public ObservableField<String> errorResponse = new ObservableField<>();

    @Override
    public int getLayoutId() {
        return R.layout.layout_auth;
    }

    public void register(View view) {
        show(new RegisterController());
    }

    public void auth(View view) {
        Request.with(this, Api.class)
                .create(api -> api.authByPhone(phone.get(), password.get()))
                .onError(error -> {
                    errorResponse.set(error.getMessage());
                })
                .execute(user -> {
                    show(new MyScheduleController());
                });
    }

    public void onForgotPasswordClicked(View view) {

    }
}
