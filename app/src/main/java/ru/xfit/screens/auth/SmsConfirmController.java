package ru.xfit.screens.auth;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.controllers.Request;

import java.net.UnknownHostException;

import ru.xfit.R;
import ru.xfit.databinding.LayoutSmsConfirmBinding;
import ru.xfit.domain.App;
import ru.xfit.domain.MainActivity;
import ru.xfit.model.data.auth.User;
import ru.xfit.model.data.register.RegisterRequest;
import ru.xfit.model.data.storage.preferences.PreferencesManager;
import ru.xfit.model.retrorequest.NetworkError;
import ru.xfit.model.service.Api;
import ru.xfit.screens.XFitController;

/**
 * Created by TESLA on 27.10.2017.
 */

public class SmsConfirmController extends XFitController<LayoutSmsConfirmBinding> {

    public final ObservableBoolean progress = new ObservableBoolean();
    public ObservableField<String> code1 = new ObservableField<>("");
    public ObservableField<String> code2 = new ObservableField<>("");
    public ObservableField<String> code3 = new ObservableField<>("");
    public ObservableField<String> code4 = new ObservableField<>("");
    public ObservableField<String> code5 = new ObservableField<>("");
    public ObservableField<String> code6 = new ObservableField<>("");
    public ObservableField<String> errorResponse = new ObservableField<>();
    private RegisterRequest regData;

    public SmsConfirmController(RegisterRequest regData) {
        this.regData = regData;
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_sms_confirm;
    }

    public void activate(View view) {
        StringBuilder code = new StringBuilder();
        code.append(code1.get());
        code.append(code2.get());
        code.append(code3.get());
        code.append(code4.get());
        code.append(code5.get());
        code.append(code6.get());

        regData.phoneConfirmation = code.toString();

        Request.with(this, Api.class)
                .create(api -> {
                    progress.set(true);
                    return api.register(regData);})
                .onError(error -> {
                    if (error instanceof NetworkError) {
                        NetworkError netError = (NetworkError) error;
                        String errorText;
                        switch (netError.getCode()) {
                            case 503:
                                errorText = view.getContext().getString(R.string.sms_wrong_code);
                                break;
                            case 500:
                                errorText = view.getContext().getString(R.string.sms_wrong_code);
                                break;
                            default:
                                errorText = netError.getMessage();
                        }
                        Snackbar.make(view, errorText, BaseTransientBottomBar.LENGTH_INDEFINITE)
                                .setAction("Ok", view1 -> {
                                })
                                .show();
                        Snackbar.make(view, ((NetworkError)error).getErrorResponse().message, BaseTransientBottomBar.LENGTH_INDEFINITE)
                                .setAction("Ok", view1 -> {})
                                .show();
                    } else if (error instanceof UnknownHostException) {
                        Snackbar.make(view, view.getContext().getResources().getString(R.string.auth_internet_error), BaseTransientBottomBar.LENGTH_INDEFINITE)
                                .setAction("Ok", view1 -> {})
                                .show();
                    } else {
                        Snackbar.make(view, error.getMessage(), BaseTransientBottomBar.LENGTH_INDEFINITE)
                                .setAction("Ok", view1 -> {})
                                .show();
                    }

                })
                .onFinally(() -> progress.set(false))
                .execute(registrationResponse -> {
                    //save user
                    PreferencesManager manager = new PreferencesManager(App.getContext());
                    manager.putValue(PreferencesManager.KEY_IS_USER_ALREADY_LOGIN, true);

                    registrationResponse.user.language = registrationResponse.language;
                    registrationResponse.user.city = registrationResponse.city;
                    registrationResponse.user.token = registrationResponse.token;

                    saveUser(registrationResponse.user);

                    MainActivity.start(getActivity());
                    getActivity().finish();
                });
    }

    private void saveUser(User user) {
        Request.with(this, Api.class)
                .create(api -> api.saveUser(user))
                .execute();
    }

    public void back(View view) {
        back();
    }

}
