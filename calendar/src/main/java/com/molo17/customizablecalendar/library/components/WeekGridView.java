package com.molo17.customizablecalendar.library.components;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.molo17.customizablecalendar.library.adapter.WeekAdapter;
import com.molo17.customizablecalendar.library.interactors.ViewInteractor;
import com.molo17.customizablecalendar.library.presenter.interfeaces.CustomizableCalendarPresenter;
import com.molo17.customizablecalendar.library.view.BaseView;

import org.joda.time.DateTime;

/**
 * Created by TESLA on 10.11.2017.
 */

public class WeekGridView extends LinearLayout implements BaseView {
    protected DateTime monthDateTime;
    private WeekAdapter calendarAdapter;
    private GridView calendarGrid;
    private DateTime currentMonth;

    public WeekGridView(Context context) {
        super(context);
    }

    public WeekGridView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WeekGridView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void refreshData() {

    }

    @Override
    public void setLayoutResId(@LayoutRes int layoutResId) {

    }

    @Override
    public void injectViewInteractor(ViewInteractor viewInteractor) {

    }

    @Override
    public void injectPresenter(CustomizableCalendarPresenter presenter) {

    }
}
