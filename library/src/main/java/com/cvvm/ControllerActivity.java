package com.cvvm;

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
