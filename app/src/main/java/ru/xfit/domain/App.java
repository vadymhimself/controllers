package ru.xfit.domain;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.controllers.Request;
import com.hwangjr.rxbus.Bus;
import ru.xfit.model.service.Api;
import ru.xfit.model.service.DaggerModelComponent;

import javax.inject.Inject;

/**
 * Created by Vadym Ovcharenko
 * 27.10.2016.
 */

public class App extends Application {
    public static final String PREFERENCES = "_app_prefs_ru.xfit_prettyPrefs";
    public static final String PREFS_IS_USER_ALREADY_LOGIN = "is_user_already_login";

    @SuppressLint("StaticFieldLeak")
    private static App context;

    private static XFitComponent injector;

    @Inject Bus bus;
    @Inject Api api;

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;

        injector = DaggerXFitComponent.builder()
                .modelComponent(DaggerModelComponent.create())
                .addModule(new DataModule(context))
                .build();

        injector.inject(this);

        Request.registerService(api);

        Request.setDefaultErrorAction(e -> {
            Log.e("Request", "error: ", e);
            Toast.makeText(this, e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        });
    }

    public static Context getContext() {
        return context;
    }

    static XFitComponent getInjector() {
        return injector;
    }

    public static Bus getBus() {
        return context.bus;
    }

}
