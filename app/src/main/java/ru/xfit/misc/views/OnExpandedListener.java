package ru.xfit.misc.views;

/**
 * Created by aleks on 28.11.2017.
 */

import ru.xfit.misc.adapters.BaseVM;

public interface OnExpandedListener<T extends BaseVM> {
    void onExpanded(T t);
}
