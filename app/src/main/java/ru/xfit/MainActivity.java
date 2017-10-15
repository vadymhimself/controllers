package ru.xfit;

import android.os.Bundle;
import android.view.View;
import com.controllers.ControllerActivity;
import ru.xfit.screens.BusTestController;
import ru.xfit.screens.HomeController;

public class MainActivity extends XFitActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setControllerContainer(R.id.container);
        if (savedInstanceState == null) {
            show(new HomeController());
        }
    }
}
