package ru.xfit.domain;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import ru.xfit.R;
import ru.xfit.model.data.storage.preferences.PreferencesManager;
import ru.xfit.screens.auth.AuthController;

/**
 * Created by TESLA on 25.10.2017.
 */

public class StartActivity extends XFitActivity {
    public static void start(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_start);
        setControllerContainer(R.id.container);

        PreferencesManager preferencesManager = new PreferencesManager(this);

        if (preferencesManager.getBoolean(PreferencesManager.KEY_IS_USER_ALREADY_LOGIN)) {
            MainActivity.start(this);
            finish();
        } else {
            show(new AuthController());
        }
    }
}
