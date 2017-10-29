package ru.xfit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.controllers.ControllerActivity;
import ru.xfit.screens.BusTestController;
import ru.xfit.screens.HomeController;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends XFitActivity {
    public static void start(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        setControllerContainer(R.id.container);
        if (savedInstanceState == null) {
            show(new HomeController());
        }
    }
}
