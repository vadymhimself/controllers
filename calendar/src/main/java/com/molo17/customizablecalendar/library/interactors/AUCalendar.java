package com.molo17.customizablecalendar.library.interactors;

import com.molo17.customizablecalendar.library.model.Calendar;
import com.molo17.customizablecalendar.library.model.CalendarFields;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;

/**
 * Created by francescofurlan on 30/06/17.
 */

public class AUCalendar {
    private static AUCalendar AUCalendarInstance;
    private Calendar calendar;
    public List<DateTime> highLightDateTimes = new ArrayList<>();
    private List<CalendarObjectChangeListener> onChangeListenerList = new ArrayList<>();

    public static AUCalendar getInstance() {
        if (AUCalendarInstance == null) {
            AUCalendarInstance = new AUCalendar();
        }
        return AUCalendarInstance;
    }

    public static AUCalendar getInstance(Calendar calendar) {
        getInstance();
        if (calendar != null) {
            AUCalendarInstance.setCalendar(calendar);
        }
        return AUCalendarInstance;
    }

    public DateTime getFirstMonth() {
        return calendar.getFirstMonth();
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void highLightDates(List<DateTime> dateTimes) {
        highLightDateTimes = dateTimes;
        emitOnChange(CalendarFields.HIGHLIGHTED_DAYS);
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public void setToday(DateTime today) {
        calendar.setToday(today);
    }

    public DateTime getToday() {
        return calendar.getToday();
    }

    public Flowable<ChangeSet> observeChangesOnCalendar() {
        return Flowable.create((FlowableEmitter<ChangeSet> emitter) -> {
            CalendarObjectChangeListener objectChangeListener = emitter::onNext;
            addChangeListener(objectChangeListener);
            emitter.setCancellable(() -> removeChangeListener(objectChangeListener));
        }, BackpressureStrategy.BUFFER);
    }

    private void addChangeListener(CalendarObjectChangeListener objectChangeListener) {
        onChangeListenerList.add(objectChangeListener);
    }

    private void removeChangeListener(CalendarObjectChangeListener objectChangeListener) {
        onChangeListenerList.remove(objectChangeListener);
    }

    private void emitOnChange(ChangeSet changeSet) {
        for (CalendarObjectChangeListener onChangeListener : onChangeListenerList) {
            onChangeListener.onChange(changeSet);
        }
    }

    private void emitOnChange(String changedField) {
        CalendarChangeSet changeSet = new CalendarChangeSet();
        changeSet.addChangedField(changedField);
        emitOnChange(changeSet);
    }

    public DateTime getFirstSelectedDay() {
        return calendar.getFirstSelectedDay();
    }

    public void setFirstSelectedDay(DateTime firstSelectedDay) {
        if (calendar.getFirstSelectedDay() != firstSelectedDay) {
            calendar.setFirstSelectedDay(firstSelectedDay);
            emitOnChange(CalendarFields.FIRST_SELECTED_DAY);
        }
    }

    public DateTime getLastSelectedDay() {
        return calendar.getLastSelectedDay();
    }

    public void setLastSelectedDay(DateTime lastSelectedDay) {
        if (calendar.getLastSelectedDay() != lastSelectedDay) {
            calendar.setLastSelectedDay(lastSelectedDay);
            emitOnChange(CalendarFields.LAST_SELECTED_DAY);
        }
    }

    public DateTime getCurrentMonth() {
        return calendar.getCurrentMonth();
    }

    public void setCurrentMonth(DateTime currentMonth) {
        if (calendar.getCurrentMonth() != currentMonth) {
            calendar.setCurrentMonth(currentMonth);
            emitOnChange(CalendarFields.CURRENT_MONTH);
        }
    }

    public List<DateTime> getMonths() {
        return calendar.getMonths();
    }

    public List<DateTime> getWeeks() {
        return calendar.getWeeks();
    }

    public void setMonths(List<DateTime> months) {
        if (calendar.getMonths() != months) {
            calendar.setMonths(months);
            emitOnChange(CalendarFields.MONTHS);
        }
    }

    public void setWeeks(List<DateTime> weeks) {
        if (calendar.getWeeks() != weeks) {
            calendar.setWeeks(weeks);
            emitOnChange(CalendarFields.WEEKS);
        }
    }

    public boolean isMultipleSelectionEnabled() {
        return calendar.isMultipleSelectionEnabled();
    }

    public int maxDaysForSelection() {
        return calendar.getMaxDaysSelection();
    }

    public void setMaxDaysSelection(int days) {
        calendar.setMaxDaysSelection(days);
    }

    public int minDaysForSelection() {
        return calendar.getMinDaysSelection();
    }

    public void setMultipleSelection(boolean multipleSelection) {
        if (calendar.isMultipleSelectionEnabled() != multipleSelection) {
            calendar.setMultipleSelection(multipleSelection);
            emitOnChange(CalendarFields.MULTIPLE_SELECTION);
        }
    }

    public int getFirstDayOfWeek() {
        return calendar.getFirstDayOfWeek();
    }

    public void setFirstDayOfWeek(int firstDayOfWeek) {
        if (calendar.getFirstDayOfWeek() != firstDayOfWeek) {
            calendar.setFirstDayOfWeek(firstDayOfWeek);
            emitOnChange(CalendarFields.FIRST_DAY_OF_WEEK);
        }
    }

    public interface ChangeSet {
        List<String> getChangedFields();

        boolean isFieldChanged(String fieldName);

        String toString();
    }

    public interface CalendarObjectChangeListener {
        void onChange(ChangeSet changeSet);
    }

    private class CalendarChangeSet implements ChangeSet {
        private List<String> changedFields = new ArrayList<>();

        public void setChangedFiels(List<String> changedFields) {
            this.changedFields = changedFields;
        }

        public void addChangedField(String changedField) {
            this.changedFields.add(changedField);
        }

        @Override
        public List<String> getChangedFields() {
            return changedFields;
        }

        @Override
        public boolean isFieldChanged(String fieldName) {
            return changedFields.contains(fieldName);
        }

        @Override
        public String toString() {
            String fieldChanges = "";
            for (String fieldChanged : changedFields) {
                fieldChanges += " " + fieldChanged;
            }
            return fieldChanges;
        }
    }
}