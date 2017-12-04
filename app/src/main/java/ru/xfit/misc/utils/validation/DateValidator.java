package ru.xfit.misc.utils.validation;

import java.util.Calendar;
import java.util.GregorianCalendar;

import ru.xfit.R;
import ru.xfit.domain.App;

/**
 * Created by aleks on 04.12.2017.
 */

public class DateValidator extends StringValidator {

    private Calendar currentCalendar;

    public DateValidator() {
        currentCalendar = Calendar.getInstance();
    }

    @Override
    public String validate(String... args) {
        String result = super.validate(args);
        if (result != null)
            return result;
        if (args[0].length() != 8)
            return App.getContext().getString(R.string.incorrect_date);
        try {
            Calendar calendar = new GregorianCalendar();
            int day = Integer.valueOf(args[0].substring(0, 2));
            calendar.set(Calendar.DAY_OF_MONTH, day);
            int month = Integer.valueOf(args[0].substring(2, 4));
            calendar.set(Calendar.MONTH, month - 1);
            int year = Integer.valueOf(args[0].substring(4, 8));
            calendar.set(Calendar.YEAR, year);
            if (calendar.compareTo(currentCalendar) > 0)
                return App.getContext().getString(R.string.incorrect_date);
            if (calendar.get(Calendar.YEAR) < 1900)
                return App.getContext().getString(R.string.incorrect_date);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
            return App.getContext().getString(R.string.incorrect_date);
        }
        return null;
    }
}
