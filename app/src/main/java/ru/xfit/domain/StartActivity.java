package ru.xfit.domain;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.controllers.Controller;

import ru.xfit.BuildConfig;
import ru.xfit.R;
import ru.xfit.model.data.storage.preferences.PreferencesManager;
import ru.xfit.screens.auth.AuthController;

/**
 * Created by TESLA on 25.10.2017.
 */

public class StartActivity extends XFitActivity {
    public static void start(Context context) {
        Intent intent = new Intent(context, StartActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_start);
        setControllerContainer(R.id.container);

        TextView buildVersion = (TextView) findViewById(R.id.build_ver);
        buildVersion.setText(getVersionName());


        if (ActivityCompat.checkSelfPermission(App.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(App.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            init();
        } else {
            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissions, 1);
            }
        }
    }

    private void init() {
        PreferencesManager preferencesManager = new PreferencesManager(this);

        if (preferencesManager.getBoolean(PreferencesManager.KEY_IS_USER_ALREADY_LOGIN)) {
            MainActivity.start(this);
            finish();
        } else {
            show(new AuthController(), 0, 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1)
            init();
    }

    public String getVersionName() {
        return "build: " + BuildConfig.VERSION_NAME;
    }

    @Override
    public void onBackPressed() {
        if (stack.size() > 1)
            back(R.anim.slidein_left, R.anim.slideout_right);
        else
            finish();
    }

    @Override
    protected Controller show(@NonNull Controller controller) {
        return show(controller, R.anim.slidein_right, R.anim.slideout_left);
    }

    @Override
    protected Controller back() {
        return back(R.anim.slidein_left, R.anim.slideout_right);
    }
}
