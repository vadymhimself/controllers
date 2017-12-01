package com.molo17.customizablecalendar.library.model;

import com.molo17.customizablecalendar.library.utils.DateUtils;

import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.Weeks;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by francescofurlan on 27/06/17.
 */

public class Calendar {
    private DateTime firstMonth;
    private DateTime firstSelectedDay;
    private DateTime lastSelectedDay;
    private DateTime currentMonth;
    private DateTime today;
    private List<DateTime> months;
    private List<DateTime> weeks;
    private boolean multipleSelection;
    private int firstDayOfWeek;
    private int maxDaysSelection;
    private int minDaysSelection;

    public Calendar(DateTime firstMonth, DateTime lastMonth) {
        this.firstMonth = firstMonth;
        this.firstDayOfWeek = java.util.Calendar.getInstance(DateUtils.myLocale).getFirstDayOfWeek();

        int startMonth = firstMonth.getMonthOfYear();
        int monthsBetweenCount = Months.monthsBetween(firstMonth, lastMonth).getMonths();
        int weeksBetweenCount = Weeks.weeksBetween(firstMonth, lastMonth).getWeeks();

        months = new ArrayList<>();
        weeks = new ArrayList<>();

        months.add(firstMonth);
        currentMonth = firstMonth;

        DateTime monthToAdd = new DateTime(firstMonth.getYear(), startMonth, 1, 0, 0);
        for (int i = 0; i <= monthsBetweenCount; i++) {
            months.add(monthToAdd);
            monthToAdd = monthToAdd.plusMonths(1);
        }

        DateTime today = new DateTime();
        int startWeekDay = today.getDayOfMonth() - (today.getDayOfWeek() - 1);
        startMonth = firstMonth.getMonthOfYear();
        DateTime weekToAdd = new DateTime(firstMonth.getYear(), startMonth, startWeekDay, 0, 0);
        for (int i = 0; i <= weeksBetweenCount; i++) {
            weeks.add(weekToAdd);
            weekToAdd = weekToAdd.plusWeeks(1);
        }
    }

    public DateTime getFirstSelectedDay() {
        return firstSelectedDay;
    }

    public void setFirstSelectedDay(DateTime firstSelectedDay) {
        this.firstSelectedDay = firstSelectedDay;
    }

    public DateTime getLastSelectedDay() {
        return lastSelectedDay;
    }

    public void setLastSelectedDay(DateTime lastSelectedDay) {
        this.lastSelectedDay = lastSelectedDay;
    }

    public DateTime getCurrentMonth() {
        return currentMonth;
    }

    public void setCurrentMonth(DateTime currentMonth) {
        this.currentMonth = currentMonth;
    }

    public String getCurrentYear() {
        return firstMonth.toString("yyyy", DateUtils.myLocale);
    }

    public int getCurrentWeek() {
        return firstMonth.getWeekOfWeekyear();
    }

    public List<DateTime> getMonths() {
        return months;
    }

    public void setMonths(List<DateTime> months) {
        this.months = months;
    }

    public List<DateTime> getWeeks() {
        return weeks;
    }

    public void setWeeks(List<DateTime> weeks) {
        this.weeks = weeks;
    }

    public boolean isMultipleSelectionEnabled() {
        return multipleSelection;
    }

    public void setMultipleSelection(boolean multipleSelection) {
        this.multipleSelection = multipleSelection;
    }

    public int getFirstDayOfWeek() {
        return firstDayOfWeek;
    }

    public void setFirstDayOfWeek(int firstDayOfWeek) {
        this.firstDayOfWeek = firstDayOfWeek;
    }

    public DateTime getFirstMonth() {
        return firstMonth;
    }

    public DateTime getToday() {
        return today;
    }

    public void setToday(DateTime today) {
        this.today = today;
    }

    public int getMaxDaysSelection() {
        return maxDaysSelection;
    }

    public void setMaxDaysSelection(int maxDaysSelection) {
        this.maxDaysSelection = maxDaysSelection;
    }

    public int getMinDaysSelection() {
        return minDaysSelection;
    }

    public void setMinDaysSelection(int minDaysSelection) {
        this.minDaysSelection = minDaysSelection;
    }
}
