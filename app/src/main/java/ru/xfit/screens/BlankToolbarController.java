package ru.xfit.screens;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.Toolbar;

import ru.xfit.misc.NavigationClickListener;

/**
 * Created by TESLA on 13.11.2017.
 */

public abstract class BlankToolbarController<B extends ViewDataBinding> extends XFitController<B>
        implements Toolbar.OnMenuItemClickListener, NavigationClickListener {
}
