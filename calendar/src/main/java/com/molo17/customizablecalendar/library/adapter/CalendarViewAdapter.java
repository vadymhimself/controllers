package com.molo17.customizablecalendar.library.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.molo17.customizablecalendar.library.R;
import com.molo17.customizablecalendar.library.components.MonthGridView;
import com.molo17.customizablecalendar.library.interactors.AUCalendar;
import com.molo17.customizablecalendar.library.interactors.ViewInteractor;
import com.molo17.customizablecalendar.library.presenter.interfeaces.CustomizableCalendarPresenter;
import com.molo17.customizablecalendar.library.view.BaseView;
import com.molo17.customizablecalendar.library.viewholders.CalendarViewHolder;
import com.molo17.customizablecalendar.library.viewholders.MonthViewHolder;

import org.joda.time.DateTime;

/**
 * Created by francescofurlan on 23/06/17.
 */

public class CalendarViewAdapter extends RecyclerView.Adapter<CalendarViewHolder> implements BaseView {
    private Context context;
    private AUCalendar calendar;
    private int layoutResId = -1;
    private int dayLayoutResId = -1;
    private ViewInteractor viewInteractor;

    public CalendarViewAdapter(Context context) {
        this.context = context;
        this.calendar = AUCalendar.getInstance();
    }

    @Override
    public int getItemCount() {
        return calendar.getMonths().size();
    }

    @Override
    public CalendarViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View rootView;
        if (layoutResId != -1 && layoutResId != R.layout.calendar_view) {
            rootView = LayoutInflater.from(context).inflate(layoutResId, null);
            rootView = viewInteractor.getMonthGridView(rootView);
        } else {
            MonthGridView monthGridView = new MonthGridView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            monthGridView.setLayoutParams(params);
            rootView = monthGridView;
        }

        if (viewInteractor != null) {
            viewInteractor.onMonthBindView(rootView);
        }
        return new MonthViewHolder(rootView, layoutResId, dayLayoutResId, viewInteractor);
    }

    @Override
    public void onBindViewHolder(final CalendarViewHolder viewHolder, final int position) {
        DateTime currentMonth = calendar.getMonths().get(position);
        if (viewHolder.getClass() == MonthViewHolder.class)
            ((MonthViewHolder)viewHolder).monthView.setCurrentMonth(currentMonth);
    }

    @Override
    public void refreshData() {
        notifyDataSetChanged();
    }

    @Override
    public void setLayoutResId(@LayoutRes int layoutResId) {
        this.layoutResId = layoutResId;
    }

    public void setDayLayoutResId(@LayoutRes int layoutResId) {
        this.dayLayoutResId = layoutResId;
    }

    @Override
    public void injectViewInteractor(ViewInteractor viewInteractor) {
        this.viewInteractor = viewInteractor;
    }

    @Override
    public void injectPresenter(CustomizableCalendarPresenter presenter) {
    }

}