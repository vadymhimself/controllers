package ru.xfit.misc.utils.validation;

import java.util.regex.Pattern;

import ru.xfit.R;
import ru.xfit.domain.App;

/**
 * Created by aleks on 04.12.2017.
 */

public class NameSurnameValidator extends StringValidator {
    public static final Pattern NAME = Pattern.compile("[a-zA-Zа-яА-Я\\-\\s]{3,65}");

    @Override
    public String validate(String... args) {
        String result = super.validate(args);
        if (result != null)
            return result;
        if (args[0].length() < 3)
            return App.getContext().getString(R.string.short_name);
        if (args[0].length() > 65)
            return App.getContext().getString(R.string.long_name);
        if (!NAME.matcher(args[0]).matches())
            return App.getContext().getString(R.string.incorrect_name);
        return null;
    }
}
