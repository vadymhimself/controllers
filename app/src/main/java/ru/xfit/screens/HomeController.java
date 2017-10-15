package ru.xfit.screens;

import android.widget.Toast;
import com.controllers.Promise;
import com.controllers.Request;
import com.hwangjr.rxbus.annotation.Subscribe;
import ru.xfit.R;
import ru.xfit.databinding.LayoutHomeBinding;
import ru.xfit.domain.App;
import ru.xfit.model.service.Api;

public class HomeController extends XFitController<LayoutHomeBinding> {

    public HomeController() {

    }

    public void onButtonClick() {
        show(new BusTestController());
    }

    @Subscribe
    public void onTestEvent(BusTestController.TestEvent e) {
        Toast.makeText(App.getContext(), e.name, Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_home;
    }
}
