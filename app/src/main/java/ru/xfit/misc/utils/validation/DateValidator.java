package ru.xfit.misc.utils.validation;

import java.text.SimpleDateFormat;
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
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyy");
            simpleDateFormat.setLenient(false);
            calendar.setTime(simpleDateFormat.parse(args[0]));
            if (calendar.compareTo(currentCalendar) > 0)
                return App.getContext().getString(R.string.incorrect_date);
            if (calendar.get(Calendar.YEAR) < 1900)
                return App.getContext().getString(R.string.incorrect_date);
        } catch (Exception ex) {
            return App.getContext().getString(R.string.incorrect_date);
        }
        return null;
    }
}
