package com.molo17.customizablecalendar.library.viewholders;

import android.view.View;

import com.molo17.customizablecalendar.library.adapter.AdapterType;
import com.molo17.customizablecalendar.library.components.MonthGridView;
import com.molo17.customizablecalendar.library.interactors.ViewInteractor;

import org.joda.time.DateTime;

/**
 * Created by TESLA on 05.11.2017.
 */

public class MonthViewHolder extends CalendarViewHolder {
    public MonthGridView monthView;

    public MonthViewHolder(View view, int layoutResId, int dayLayoutResId, ViewInteractor viewInteractor, AdapterType adapterType, DateTime startDate, DateTime endDate) {
        super(view);
        monthView = (MonthGridView) view;
        monthView.setPreselectDates(startDate, endDate);
        monthView.setAdapterType(adapterType);
        monthView.setLayoutResId(layoutResId);
        monthView.setDayLayoutResId(dayLayoutResId);
        monthView.injectViewInteractor(viewInteractor);
    }
}
