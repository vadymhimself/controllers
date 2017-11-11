package ru.xfit.screens.filter;

import ru.xfit.R;
import ru.xfit.misc.adapters.BaseVM;

/**
 * Created by TESLA on 06.11.2017.
 */

public class HeaderFilterVM extends FilterVM implements BaseVM {
    public FilterController controller;
    public boolean headerSpace = true;
    public String title;

    public HeaderFilterVM(FilterController controller, boolean headerSpace, String title) {
        this.controller = controller;
        this.headerSpace = headerSpace;
        this.title = title;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_filter_header;
    }
}
