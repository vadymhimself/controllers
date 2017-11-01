package ru.xfit.misc.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ru.xfit.R;
import ru.xfit.misc.utils.CalendarUtils;

/**
 * Created by TESLA on 31.10.2017.
 */

public class MyCalendar extends LinearLayout {
    private static final int SUNDAY = 1;
    private static final int MONDAY = 2;
    private static final int TUESDAY = 4;
    private static final int WEDNESDAY = 8;
    private static final int THURSDAY = 16;
    private static final int FRIDAY = 32;
    private static final int SATURDAY = 64;

    private static final int[] FLAGS = new int[]{
            SUNDAY,
            MONDAY,
            TUESDAY,
            WEDNESDAY,
            THURSDAY,
            FRIDAY,
            SATURDAY
    };

    private static final int[] WEEK_DAYS = new int[]{
            Calendar.SUNDAY,
            Calendar.MONDAY,
            Calendar.TUESDAY,
            Calendar.WEDNESDAY,
            Calendar.THURSDAY,
            Calendar.FRIDAY,
            Calendar.SATURDAY
    };

    private Context context;
    private Calendar calendar;
    private Calendar prevCalendar;
    private View currentView;

    private boolean isCommonDay;
    private boolean isOverflowDateVisible = true;
    private int[] totalDayOfWeekend;

    private LinearLayout daysView;

    private int currentMonthIndex;
    private int firstDayOfWeek;
    private int weekendDays;

    private OnDateClickListener onDateClickListener;

    public MyCalendar(Context context) {
        super(context);
        init(context);
    }

    public MyCalendar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyCalendar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        this.calendar = Calendar.getInstance(Locale.getDefault());
        this.prevCalendar = Calendar.getInstance(Locale.getDefault());

        currentView = LayoutInflater.from(context).inflate(R.layout.calendar, this, true);
        daysView = (LinearLayout) currentView.findViewById(R.id.days_view);

        firstDayOfWeek = Calendar.MONDAY;
        this.weekendDays = 65;

        refreshCalendar(Calendar.getInstance(getLocale()));
    }

    private void drawAdapterView() {
        final List<Day> days = CalendarUtils.obtainDays(calendar, currentMonthIndex);

        int size = days.size();
        DayView dayView;
        LinearLayout weekContainer = new LinearLayout(context);
        LinearLayout.LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        weekContainer.setLayoutParams(lp);

        for (int i = 0; i < size; i++) {
            Day day = days.get(i);

            if (i == 7 || i == 14 || i == 21 || i == 28 || i == 35 || i == 42) {
                daysView.addView(weekContainer);
                weekContainer = new LinearLayout(context);
                weekContainer.setLayoutParams(lp);
            }

            dayView = new DayView(context);
            weekContainer.addView(dayView);

            dayView.bind(day.toDate());
            dayView.setVisibility(View.VISIBLE);

            LinearLayout.LayoutParams dlp = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            dlp.weight = 7.0f;
            dlp.gravity = Gravity.CENTER_HORIZONTAL;
            dayView.setLayoutParams(dlp);

            if (day.isCurrentMonth()) {
                isCommonDay = true;

                if (totalDayOfWeekend.length != 0) {
                    final Calendar calendar = day.toCalendar(Locale.getDefault());

                    for (int weekend : totalDayOfWeekend) {
                        if (weekend == calendar.get(Calendar.DAY_OF_WEEK)) {
                            dayView.setTextColor(context.getResources().getColor(R.color.calendarWeekend));
                            dayView.setBold();
                            isCommonDay = false;
                        }
                    }
                }

                if(isCommonDay) {
                    dayView.setTextColor(context.getResources().getColor(R.color.calendarCommonDay));
                }

                if (day.isCurrentDay()) {
                    if (CalendarUtils.isToday(day.toCalendar())) {
                        dayView.setTextColor(context.getResources().getColor(R.color.calendarCurrentDay));
                        dayView.setTextBg(R.drawable.calendar_current_day_bg);
                    }
                }

            } else {
                dayView.setTextColor(context.getResources().getColor(R.color.calendarDisableDay));

                if (!isOverflowDateVisible)
                    dayView.setVisibility(View.INVISIBLE);
                else {
                    dayView.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public Locale getLocale() {
        return this.context.getResources().getConfiguration().locale;
    }

    public void refreshCalendar(Calendar calendar) {
        this.calendar = calendar;
        this.calendar.setFirstDayOfWeek(Calendar.MONDAY);

        final int y = calendar.get(Calendar.YEAR);
        final int m = calendar.get(Calendar.MONTH);

        currentMonthIndex = (prevCalendar.get(Calendar.YEAR) - y) * 12 + (prevCalendar.get(Calendar.MONTH) - m);

        Calendar calendarUpdate = Calendar.getInstance(Locale.getDefault());
        calendarUpdate.add(Calendar.MONTH, currentMonthIndex);

        update(calendarUpdate);

        prevCalendar = calendar;
    }

    public void update(@NonNull Calendar calender) {
        calendar = calender;
        calendar.setFirstDayOfWeek(firstDayOfWeek);

        calculateWeekEnds();

        drawAdapterView();
    }

    private void calculateWeekEnds() {
        totalDayOfWeekend = new int[2];
        int weekendIndex = 0;

        for (int i = 0; i < FLAGS.length; i++) {
            boolean isContained = containsFlag(this.weekendDays, FLAGS[i]);

            if (isContained) {
                totalDayOfWeekend[weekendIndex] = WEEK_DAYS[i];
                weekendIndex++;
            }
        }
    }

    public void setOnDateClickListener(OnDateClickListener onDateClickListener) {
        this.onDateClickListener = onDateClickListener;
    }

    private boolean containsFlag(int flagSet, int flag) {
        return (flagSet | flag) == flagSet;
    }
}
