package com.cvvm;

/**
 * Created by Vadym Ovcharenko
 * 21.10.2016.
 */

public abstract class ControllerActivity extends AbstractControllerActivity {

    // TODO: WTF?
    @Override
    boolean beforeControllersChanged(AbstractController previous, AbstractController next) {
        return this.beforeControllersChanged((Controller)previous, (Controller)next);
    }

    protected boolean beforeControllersChanged(Controller previous, Controller next) {
        return false;
    }
}
