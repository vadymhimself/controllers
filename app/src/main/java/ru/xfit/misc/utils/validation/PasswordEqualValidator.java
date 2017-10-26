package ru.xfit.misc.utils.validation;

import ru.xfit.R;
import ru.xfit.domain.App;

/**
 * Created by TESLA on 27.10.2017.
 */

public class PasswordEqualValidator extends StringValidator {

    @Override
    public String validate(String... args) {
        if (!args[0].equals(args[1]))
            return App.getContext().getString(R.string.password_not_equal);
        return null;
    }
}