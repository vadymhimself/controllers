package ru.xfit.screens.auth;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.controllers.Request;

import java.net.UnknownHostException;

import ru.xfit.R;
import ru.xfit.databinding.LayoutRegisterBinding;
import ru.xfit.domain.App;
import ru.xfit.domain.StartActivity;
import ru.xfit.misc.EditDoneListener;
import ru.xfit.misc.utils.UiUtils;
import ru.xfit.model.data.register.RegisterRequest;
import ru.xfit.model.service.Api;
import ru.xfit.screens.XFitController;

/**
 * Created by TESLA on 25.10.2017.
 */

public class RegisterController extends XFitController<LayoutRegisterBinding> implements EditDoneListener {
    public ObservableField<String> phone = new ObservableField<>("");
    public ObservableField<String> date = new ObservableField<>("");
    public ObservableField<String> password = new ObservableField<>("");
    public ObservableField<String> passwordRepeat = new ObservableField<>("");
    public ObservableField<String> name = new ObservableField<>("");
    public ObservableField<String> email = new ObservableField<>("");
    public ObservableInt gender = new ObservableInt(R.id.male);

    public ObservableField<String> phoneError = new ObservableField<>(null);
    public ObservableField<String> emailError = new ObservableField<>(null);
    public ObservableField<String> passwordError = new ObservableField<>(null);
    public ObservableField<String> nameError = new ObservableField<>(null);
    public ObservableField<String> rePasswordError = new ObservableField<>(null);
    public ObservableField<String> dateError = new ObservableField<>(null);

    public ObservableBoolean isPhoneErrorVisible = new ObservableBoolean();
    public ObservableBoolean isEmailErrorVisible = new ObservableBoolean();
    public ObservableBoolean isPasswordErrorVisible = new ObservableBoolean();
    public ObservableBoolean isNameErrorVisible = new ObservableBoolean();
    public ObservableBoolean isRePasswordErrorVisible = new ObservableBoolean();
    public ObservableBoolean isDateErrorVisible = new ObservableBoolean();

    public ObservableBoolean progress = new ObservableBoolean();
    public ObservableField<String> errorResponse = new ObservableField<>();

    @Override
    public int getLayoutId() {
        return R.layout.layout_register;
    }

    public void back(View view) {
        back();
    }

    public void register(View view) {
        initValidation();
        if (validate()) {
            UiUtils.hideKeyboard(getActivity());

            progress.set(true);
            RegisterRequest regData = new RegisterRequest();
            regData.phone = phone.get();
            regData.birthday = parseDate();
            regData.password = password.get();
            regData.name = clearName();
            regData.email = clearEmail();
            if (gender.get() == R.id.male)
                regData.gender = "male";
            else
                regData.gender = "female";

            Request.with(this, Api.class)
                    .create(api -> api.pleaseConfirm(regData.phone))
                    .onError(error -> {
                        if (error instanceof UnknownHostException) {
                            errorResponse.set(App.getContext().getResources().getString(R.string.auth_internet_error));

                        } else {
                            errorResponse.set(error.getMessage());
                        }
                        Snackbar.make(view, errorResponse.get(), BaseTransientBottomBar.LENGTH_LONG).show();
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

    private void initValidation() {
        isPhoneErrorVisible.set(true);
        isEmailErrorVisible.set(true);
        isPasswordErrorVisible.set(true);
        isNameErrorVisible.set(true);
        isRePasswordErrorVisible.set(true);
        isDateErrorVisible.set(true);
    }

    private boolean validate() {
        return phoneError.get() == null
                && emailError.get() == null
                && passwordError.get() == null
                && nameError.get() == null
                && rePasswordError.get() == null
                && dateError.get() == null;
    }

    private String parseDate() {
        //01.34.6789  YYYY-MM-DD
        return date.get().substring(4, 8) + "-" + date.get().substring(2, 4) + "-" + date.get().substring(0, 2);
    }

    private String clearName() {
        String result = name.get();
        result = result.replaceAll("\\s{2,}", " ");
        result = result.replaceAll("\\s*-{1,}\\s*", "-");
        result = result.replaceAll("-{2,}", "-");
        String[] strings = result.split("\\s");
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < strings.length; i++) {
            if (strings[i].length() == 0)
                continue;
            String[] substrings = strings[i].split("-");
            for (int j = 0; j < substrings.length; j++) {
                char symbol = substrings[j].charAt(0);
                if (Character.isLowerCase(symbol)) {
                    stringBuilder.append(Character.toUpperCase(symbol));
                    stringBuilder.append(substrings[j].substring(1));
                } else {
                    stringBuilder.append(substrings[j]);
                }
                if (j < substrings.length - 1)
                    stringBuilder.append("-");
            }
            if (i < strings.length - 1)
                stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }

    private String clearEmail() {
        return email.get().replaceAll("\\s", "");
    }

    @Override
    public void onDoneClicked() {
        register(null);
    }
}
