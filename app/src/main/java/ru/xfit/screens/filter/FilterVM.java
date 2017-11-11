package ru.xfit.screens.filter;

import android.databinding.Bindable;
import android.databinding.ObservableBoolean;

/**
 * Created by TESLA on 08.11.2017.
 */

public abstract class FilterVM {
    public ObservableBoolean isChecked = new ObservableBoolean(true);
}
