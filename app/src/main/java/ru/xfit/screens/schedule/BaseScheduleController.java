package ru.xfit.screens.schedule;

import android.databinding.ViewDataBinding;

import java.util.ArrayList;
import java.util.List;

import ru.xfit.screens.XFitController;

/**
 * Created by TESLA on 04.11.2017.
 */

public abstract  class BaseScheduleController<B extends ViewDataBinding> extends XFitController<B> {
    protected List<MyScheduleVM> vms = new ArrayList<>();
}
