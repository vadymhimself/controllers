package ru.xfit.misc.utils.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.xfit.R;
import ru.xfit.domain.App;

/**
 * Created by TESLA on 11.11.2017.
 */

public class PhoneValidator extends StringValidator {
    @Override
    public String validate(String... args) {
        Pattern pattern = Pattern.compile("^[+]7\\s\\(\\d{3}\\)\\s\\d{3}[-]\\d{2}[-]\\d{2}$");
        Matcher matcher = pattern.matcher(args[0]);
        if (!matcher.matches())
            return App.getContext().getString(R.string.validation_incorrect_number);
        return null;
    }
}
