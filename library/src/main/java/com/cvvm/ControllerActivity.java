package com.cvvm;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Pair;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vadim Ovcharenko
 * 21.10.2016.
 */

public abstract class ControllerActivity extends AbstractControllerActivity {

    @Override
    boolean beforeControllersChanged(AbstractController previous, AbstractController next) {
        return this.beforeControllersChanged((Controller)previous, (Controller)next);
    }

    protected boolean beforeControllersChanged(Controller previous, Controller next) {
        return false;
    }
}
