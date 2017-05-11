package com.cvvm;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import java.io.Serializable;

/**
 * Created by Vadim Ovcharenko
 * 18.10.2016.
 */

interface IViewModel<C extends IController> {
    @Nullable C getController();
}
