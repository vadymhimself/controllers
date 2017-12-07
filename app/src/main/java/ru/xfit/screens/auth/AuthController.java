package ru.xfit.screens.auth;

import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.net.Uri;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.controllers.Request;

import java.net.UnknownHostException;

import ru.xfit.R;
import ru.xfit.databinding.LayoutAuthBinding;
import ru.xfit.domain.App;
import ru.xfit.domain.MainActivity;
import ru.xfit.misc.EditDoneListener;
import ru.xfit.model.data.ErrorCodes;
import ru.xfit.model.data.auth.User;
import ru.xfit.model.data.storage.preferences.PreferencesManager;
import ru.xfit.model.retrorequest.NetworkError;
import ru.xfit.model.service.Api;
import ru.xfit.screens.XFitController;


/**
 * Created by TESLA on 25.10.2017.
 */

public class AuthController extends XFitController<LayoutAuthBinding> implements EditDoneListener {

    public final ObservableBoolean progress = new ObservableBoolean();
    public ObservableField<String> phone = new ObservableField<>("");
    public ObservableField<String> password = new ObservableField<>("");
    public ObservableField<String> errorResponse = new ObservableField<>();
    public ObservableField<String> passwordError = new ObservableField<>(null);
    public ObservableField<String> telError = new ObservableField<>(null);
    public ObservableBoolean isPasswordErrorVisible = new ObservableBoolean();
    public ObservableBoolean isTelErrorVisible = new ObservableBoolean();

    @Override
    public int getLayoutId() {
        return R.layout.layout_auth;
    }

    public void register(View view) {
        show(new RegisterController());
    }

    public void auth(View view) {
        isPasswordErrorVisible.set(true);
        isTelErrorVisible.set(true);
        if (passwordError.get() == null && telError.get() == null) {
            progress.set(true);
            Request.with(this, Api.class)
                    .create(api -> api.authByPhone(phone.get(), password.get()))
                    .onFinally(() -> progress.set(false))
                    .onError(error -> {
                        if (error instanceof NetworkError) {
                            if (((NetworkError) error).getErrorResponse().code.equals(ErrorCodes.USER_ID_NOT_SPECIFIED.getErrorCode())) {
                                if (getBinding() != null) {
                                    Snackbar snackbar = Snackbar.make(view, view.getContext().getResources().getString(R.string.auth_pass_error), BaseTransientBottomBar.LENGTH_SHORT);
                                    snackbar.show();
                                }
                            } else if (((NetworkError) error).getErrorResponse().code.equals(ErrorCodes.INVALID_USER_ID_OR_PASSWORD.getErrorCode())) {
                                if (getBinding() != null) {
                                    Snackbar snackbar = Snackbar.make(view, view.getContext().getResources().getString(R.string.auth_pass_error), BaseTransientBottomBar.LENGTH_SHORT);
                                    snackbar.show();
                                }
                            } else {
                                Snackbar.make(view, error.getMessage(), BaseTransientBottomBar.LENGTH_INDEFINITE)
                                        .setAction("Ok", view1 -> {})
                                        .show();
                            }
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
                    .execute(user -> {

                        PreferencesManager preferencesManager = new PreferencesManager(App.getContext());
                        preferencesManager.putValue(PreferencesManager.KEY_IS_USER_ALREADY_LOGIN, true);

                        user.user.language = user.language;
                        user.user.city = user.city;
                        user.user.token = user.token;

                        user.user.pass = password.get();
                        user.user.phone = phone.get();

                        saveUser(user.user);

                        MainActivity.start(getActivity());
                        getActivity().finish();
                    });
        }
    }

    private void saveUser(User user) {
        Request.with(this, Api.class)
                .create(api -> api.saveUser(user))
                .execute();
    }

    public void onForgotPasswordClicked(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.xfit.ru/payonline/wnd.php?a=forgot"));
        getActivity().startActivity(intent);
    }

    @Override
    public void onDoneClicked() {
        if (getBinding() != null)
            auth(getBinding().loginBtn);
    }
}
