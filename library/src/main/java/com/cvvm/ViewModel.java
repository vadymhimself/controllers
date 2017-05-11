package com.cvvm;

import android.databinding.BaseObservable;
import android.databinding.ViewDataBinding;
import android.support.annotation.Nullable;

import java.io.Serializable;

/**
 * Created by Vadim Ovcharenko
 * 18.10.2016.
 */

public abstract class ViewModel<C extends AbstractController, B extends ViewDataBinding> extends AbstractViewModel<C, B> implements Serializable {


}
