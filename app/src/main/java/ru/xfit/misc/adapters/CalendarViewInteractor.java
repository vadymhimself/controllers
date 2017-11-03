package ru.xfit.misc.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.molo17.customizablecalendar.library.adapter.WeekDaysViewAdapter;
import com.molo17.customizablecalendar.library.interactors.ViewInteractor;
import com.molo17.customizablecalendar.library.model.Calendar;
import com.molo17.customizablecalendar.library.model.CalendarItem;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by TESLA on 03.11.2017.
 */

public class CalendarViewInteractor  implements ViewInteractor {
    private Context context;
    private Calendar calendar;
    private String firstDaySelectedTxt;
    private String lastDaySelectedTxt;

    CalendarViewInteractor(Context context) {
        this.context = context;
    }

    @Override
    public void onCustomizableCalendarBindView(View view) {

    }

    @Override
    public void onHeaderBindView(ViewGroup view) {

    }

    @Override
    public void onWeekDaysBindView(View view) {

    }

    @Override
    public void onWeekDayBindView(WeekDaysViewAdapter.WeekDayVH holder, String weekDay) {

    }

    @Override
    public void onSubViewBindView(View view, String month) {

    }

    @Override
    public void onCalendarBindView(View view) {

    }

    @Override
    public void onMonthBindView(View view) {

    }

    @Override
    public View onMonthCellBindView(View view, CalendarItem currentItem) {
        return null;
    }

    @Override
    public boolean hasImplementedDayCalculation() {
        return false;
    }

    @Override
    public List<CalendarItem> calculateDays(int year, int month, int firstDayOfMonth, int lastDayOfMonth) {
        return null;
    }

    @Override
    public boolean hasImplementedSelection() {
        return false;
    }

    @Override
    public int setSelected(boolean multipleSelection, DateTime dateSelected) {
        return 0;
    }

    @Override
    public boolean hasImplementedMonthCellBinding() {
        return false;
    }

    @Override
    public View getMonthGridView(View rootView) {
        return null;
    }

    @Override
    public boolean hasImplementedWeekDayNameFormat() {
        return false;
    }

    @Override
    public String formatWeekDayName(String nameOfDay) {
        return null;
    }

    void updateCalendar(Calendar calendar) {
        this.calendar = calendar;
        if (firstDaySelectedTxt != null && lastDaySelectedTxt != null) {
            DateTime firstDate = calendar.getFirstSelectedDay();
            DateTime lastDate = calendar.getLastSelectedDay();
            if (firstDate != null) {
                firstDaySelectedTxt = firstDate.toString("dd MMMMM yyyy");
            }
            if (lastDate != null) {
                lastDaySelectedTxt = lastDate.toString("dd MMMMM yyyy");
            }
        }
    }
}
