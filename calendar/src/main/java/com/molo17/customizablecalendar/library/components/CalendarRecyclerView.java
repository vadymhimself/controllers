package com.molo17.customizablecalendar.library.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.molo17.customizablecalendar.library.R;
import com.molo17.customizablecalendar.library.adapter.AdapterType;
import com.molo17.customizablecalendar.library.adapter.CalendarViewAdapter;
import com.molo17.customizablecalendar.library.interactors.AUCalendar;
import com.molo17.customizablecalendar.library.interactors.ViewInteractor;
import com.molo17.customizablecalendar.library.presenter.interfeaces.CustomizableCalendarPresenter;
import com.molo17.customizablecalendar.library.utils.UiUtils;
import com.molo17.customizablecalendar.library.view.CalendarView;

import org.joda.time.DateTime;

/**
 * Created by francescofurlan on 23/06/17.
 */

public class CalendarRecyclerView extends RecyclerView implements CalendarView {
    private CalendarViewAdapter weekViewAdapter;
    private CalendarViewAdapter calendarViewAdapter;
    private ViewInteractor viewInteractor;
    private Context context;
    private CustomizableCalendarPresenter presenter;
    private AUCalendar calendar;
    private DateTime startDate;
    private DateTime endDate;

    private boolean isMonthMode;

    private
    @LayoutRes
    int monthResId = R.layout.calendar_view;
    private
    @LayoutRes
    int monthCellResId = R.layout.calendar_cell;

    public CalendarRecyclerView(Context context) {
        this(context, null);
    }

    public CalendarRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.context = context;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomizableCalendar);
        if (typedArray != null) {
            monthResId = typedArray.getResourceId(R.styleable.CustomizableCalendar_month_layout, R.layout.calendar_view);
            monthCellResId = typedArray.getResourceId(R.styleable.CustomizableCalendar_cell_layout, R.layout.calendar_cell);
            typedArray.recycle();
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        setLayoutManager(linearLayoutManager);
        this.calendar = AUCalendar.getInstance();
    }

    public void setPreselectDates(DateTime startDate, DateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public void refreshData() {
        calendarViewAdapter.refreshData();
    }

    @Override
    public void setLayoutResId(@LayoutRes int layoutResId) {

    }

    @Override
    public void injectViewInteractor(ViewInteractor viewInteractor) {
        this.viewInteractor = viewInteractor;
        if (viewInteractor != null) {
            viewInteractor.onCalendarBindView(this);
        }
        setupCalendarAdapter();
        setupCalendarScroll();
    }

    @Override
    public void injectPresenter(CustomizableCalendarPresenter presenter) {
        this.presenter = presenter;
        this.presenter.injectCalendarView(this);
    }

    private void setupCalendarAdapter() {
        weekViewAdapter = new CalendarViewAdapter(context, AdapterType.TYPE_WEEK, startDate, endDate);
        weekViewAdapter.injectViewInteractor(viewInteractor);
        weekViewAdapter.setLayoutResId(monthResId);
        weekViewAdapter.setDayLayoutResId(monthCellResId);

        calendarViewAdapter = new CalendarViewAdapter(context, AdapterType.TYPE_MONTH, startDate, endDate);
        calendarViewAdapter.injectViewInteractor(viewInteractor);
        calendarViewAdapter.setLayoutResId(monthResId);
        calendarViewAdapter.setDayLayoutResId(monthCellResId);

        if (isMonthMode)
            setAdapter(calendarViewAdapter);
        else
            setAdapter(weekViewAdapter);
    }

    private void setupCalendarScroll() {
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        this.setOnFlingListener(null);
        snapHelper.attachToRecyclerView(this);
        addOnChildAttachStateChangeListener(new OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {
                MonthGridView monthGridView = (MonthGridView) view;
                monthGridView.subscribe();
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                MonthGridView monthGridView = (MonthGridView) view;
                monthGridView.unsubscribe();
            }
        });

        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case SCROLL_STATE_IDLE: {
                        View view = snapHelper.findSnapView(getLayoutManager());
                        if (view != null) {
                            int currentPosition = getChildAdapterPosition(view);
                            if (isMonthMode) {
                                DateTime currentMonth = calendar.getMonths().get(currentPosition);
                                calendar.setCurrentMonth(currentMonth);
                            } else {
                                DateTime currentMonth = calendar.getWeeks().get(currentPosition);
                                calendar.setCurrentMonth(currentMonth);
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public void setMonthLayoutResId(@LayoutRes int layoutResId) {
        calendarViewAdapter.setLayoutResId(layoutResId);
    }

    @Override
    public void setDayLayoutResId(@LayoutRes int layoutResId) {
        calendarViewAdapter.setDayLayoutResId(layoutResId);
    }

    public void setDefaultMode(boolean mode) {
        isMonthMode = mode;
    }

    @Override
    public void onMonthClicked() {
        isMonthMode = !isMonthMode;
        if (isMonthMode) {
            setupCalendarAdapter();
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UiUtils.dpToPx(context, 250f));
            this.setLayoutParams(lp);
            presenter.onMonthChanged(calendarViewAdapter.getCurrentMonth());
        } else {
            setupCalendarAdapter();
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            this.setLayoutParams(lp);
            presenter.onMonthChanged(weekViewAdapter.getCurrentMonth());
        }
    }
}