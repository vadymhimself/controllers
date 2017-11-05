package com.molo17.customizablecalendar.library.viewholders;

import android.view.View;

import com.molo17.customizablecalendar.library.components.MonthGridView;
import com.molo17.customizablecalendar.library.interactors.ViewInteractor;

/**
 * Created by TESLA on 05.11.2017.
 */

public class MonthViewHolder extends CalendarViewHolder {
    public MonthGridView monthView;

    public MonthViewHolder(View view, int layoutResId, int dayLayoutResId, ViewInteractor viewInteractor) {
        super(view);
        monthView = (MonthGridView) view;
        monthView.setLayoutResId(layoutResId);
        monthView.setDayLayoutResId(dayLayoutResId);
        monthView.injectViewInteractor(viewInteractor);
    }
}
