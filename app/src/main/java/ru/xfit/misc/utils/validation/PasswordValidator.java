package ru.xfit.misc.utils.validation;

import java.util.regex.Pattern;

import ru.xfit.R;
import ru.xfit.domain.App;

/**
 * Created by TESLA on 27.10.2017.
 */

public class PasswordValidator extends StringValidator {
    public static final Pattern PASSWORD = Pattern.compile("[a-zA-Z0-9!?*@#$%^&()\\-_+=;:,./{}\\[\\]~`|\\\\]{8,}");

    @Override
    public String validate(String... args) {
        String result = super.validate(args[0]);
        if (result != null)
            return result;
        if (!PASSWORD.matcher(args[0]).matches())
            return App.getContext().getString(R.string.incorrect_password);
        return null;

    }
}
