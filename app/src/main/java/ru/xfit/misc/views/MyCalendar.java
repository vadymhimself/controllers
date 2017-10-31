package ru.xfit.misc.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ru.xfit.R;
import ru.xfit.misc.utils.CalendarUtils;

/**
 * Created by TESLA on 31.10.2017.
 */

public class MyCalendar extends LinearLayout {
    private Context context;
    private Calendar calendar;
    private View currentView;

    private boolean isCommonDay;
    private boolean isOverflowDateVisible = true;
    private int[] totalDayOfWeekend;

    private LinearLayout daysView;

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

        currentView = LayoutInflater.from(context).inflate(R.layout.calendar, this, true);
        daysView = (LinearLayout) currentView.findViewById(R.id.days_view);

        refreshCalendar(Calendar.getInstance(getLocale()));
    }

    private void setDaysInCalendar() {
        Calendar calendar = Calendar.getInstance(getLocale());
        calendar.setTime(this.calendar.getTime());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        int firstDayOfMonth = calendar.get(Calendar.DAY_OF_WEEK);

        int dayOfMonthIndex = CalendarUtils.getWeekIndex(firstDayOfMonth, calendar);
        int actualMaximum = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        final Calendar startCalendar = (Calendar) calendar.clone();

        startCalendar.add(Calendar.DATE, -(dayOfMonthIndex - 1));
        int monthEndIndex = 42 - (actualMaximum + dayOfMonthIndex - 1);

        DayView dayView;
        LinearLayout weekContainer = new LinearLayout(context);
        LinearLayout.LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        weekContainer.setLayoutParams(lp);

        for (int i = 1; i < 43; i++) {
            if (i == 7 || i == 14 || i == 21 || i == 28 || i == 35 || i == 42) {
                weekContainer = new LinearLayout(context);
                weekContainer.setLayoutParams(lp);
            }
            dayView = new DayView(context);
            weekContainer.addView(dayView);

            if(calendar.get(Calendar.MONTH) == startCalendar.get(Calendar.MONTH)) {
                dayView.setTextColor(context.getResources().getColor(R.color.calendarCurrentDay));
                dayView.setBackgroundResource(R.drawable.calendar_current_day_bg);
            }

            //Apply the default styles
            dayView.bind(startCalendar.getTime());
            dayView.setVisibility(View.VISIBLE);


            if (CalendarUtils.isSameMonth(calendar, startCalendar)) {
//                dayOfMonthContainer.setOnClickListener(onDateClickListener);
                isCommonDay = true;
                if(totalDayOfWeekend.length != 0) {
                    for (int weekend : totalDayOfWeekend) {
                        if (startCalendar.get(Calendar.DAY_OF_WEEK) == weekend) {
                            dayView.setTextColor(context.getResources().getColor(R.color.calendarWeekend));
                            isCommonDay = false;
                        }
                    }
                }

                if(isCommonDay) {
                    dayView.setTextColor(context.getResources().getColor(R.color.calendarCommonDay));
                }
            } else {
                dayView.setTextColor(context.getResources().getColor(R.color.calendarDisableDay));

                if (!isOverflowDateVisible)
                    dayView.setVisibility(View.GONE);
                else if (i >= 36 && ((float) monthEndIndex / 7.0f) >= 1) {
                    dayView.setVisibility(View.GONE);
                }
            }

            startCalendar.add(Calendar.DATE, 1);
            dayOfMonthIndex++;
        }
    }



    public Locale getLocale() {
        return this.context.getResources().getConfiguration().locale;
    }

    public void refreshCalendar(Calendar calendar) {
        this.calendar = calendar;
        this.calendar.setFirstDayOfWeek(Calendar.MONDAY);

        setTotalDayOfWeekend();

        setDaysInCalendar();
    }

    private void setTotalDayOfWeekend() {
        int[] weekendDay = new int[Integer.bitCount(65)];
        char days[]= Integer.toBinaryString(65).toCharArray();
        int day = 1;
        int index = 0;
        for(int i = days.length - 1; i >= 0; i--) {
            if(days[i] == '1') {
                weekendDay[index] = day;
                index++;
            }
            day++;
        }

        totalDayOfWeekend = weekendDay;
    }

    public void setOnDateClickListener(OnDateClickListener onDateClickListener) {
        this.onDateClickListener = onDateClickListener;
    }
}
