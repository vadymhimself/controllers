package ru.xfit.screens;

import android.databinding.ViewDataBinding;

import java.util.ArrayList;
import java.util.List;

import ru.xfit.misc.adapters.BaseVM;
import ru.xfit.screens.schedule.MyScheduleVM;

/**
 * Created by TESLA on 08.11.2017.
 */

public abstract class DrawerController<B extends ViewDataBinding> extends XFitController<B> {
    protected List<BaseVM> vms = new ArrayList<>();
}
