package ru.xfit.screens;

import android.widget.Toast;
import com.controllers.Promise;
import com.controllers.Request;
import ru.xfit.R;
import ru.xfit.databinding.LayoutHomeBinding;
import ru.xfit.domain.App;
import ru.xfit.model.service.Api;

public class BusTestController extends XFitController<LayoutHomeBinding> {

    public BusTestController() {
        Request.with(this, Api.class)
                .create(api -> api.getUser("bolein"))
                .execute(user -> {
                    App.getBus().post(new TestEvent(user.login));
                    back();
                });
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_test;
    }

    public static class TestEvent {
        public final String name;

        public TestEvent(String name) {
            this.name = name;
        }
    }
}
