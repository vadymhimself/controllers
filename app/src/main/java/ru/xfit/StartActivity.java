package ru.xfit;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.controllers.Request;

import ru.xfit.MainActivity;
import ru.xfit.R;
import ru.xfit.XFitActivity;
import ru.xfit.misc.utils.PrefUtils;
import ru.xfit.model.service.Api;
import ru.xfit.screens.auth.AuthController;

/**
 * Created by TESLA on 25.10.2017.
 */

public class StartActivity extends XFitActivity {
    public static final String PREFS_IS_USER_ALREADY_LOGIN = "is_user_already_login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_start);
        setControllerContainer(R.id.container);


        if (PrefUtils.getPreferences().getBoolean(PREFS_IS_USER_ALREADY_LOGIN, false)) {
            MainActivity.start(this);
            finish();
        } else {
            show(new AuthController());
        }
    }
}
