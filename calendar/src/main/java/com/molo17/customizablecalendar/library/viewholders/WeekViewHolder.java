package com.molo17.customizablecalendar.library.viewholders;

import android.view.View;

import com.molo17.customizablecalendar.library.components.MonthGridView;
import com.molo17.customizablecalendar.library.interactors.ViewInteractor;

/**
 * Created by TESLA on 05.11.2017.
 */

public class WeekViewHolder extends CalendarViewHolder {
    MonthGridView weeklyView;

    public WeekViewHolder(View view, int layoutResId, int dayLayoutResId, ViewInteractor viewInteractor) {
        super(view);

        weeklyView = (MonthGridView) view;
        weeklyView.setLayoutResId(layoutResId);
        weeklyView.setDayLayoutResId(dayLayoutResId);
        weeklyView.injectViewInteractor(viewInteractor);
    }
}
