package ru.xfit.misc.utils.validation;

import java.util.regex.Pattern;

import ru.xfit.R;
import ru.xfit.domain.App;

/**
 * Created by TESLA on 27.10.2017.
 */

public class EmailValidator extends StringValidator {

    public static final Pattern EMAIL_ADDRESS
            = Pattern.compile(
            "\\s*[a-zA-Zа-яА-Я0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Zа-яА-Я0-9][a-zA-Zа-яА-Я0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Zа-яА-Я0-9][a-zA-Zа-яА-Я0-9\\-]{0,25}" +
                    ")+\\s*"
    );

    @Override
    public String validate(String... args) {
        String result = super.validate(args[0]);
        if (result != null)
            return result;
        if (!EMAIL_ADDRESS.matcher(args[0]).matches())
            return App.getContext().getString(R.string.incorrect_email);
        return null;
    }
}
