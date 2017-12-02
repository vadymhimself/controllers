package ru.xfit.misc.utils.validation;

import ru.xfit.R;
import ru.xfit.domain.App;

/**
 * Created by TESLA on 11.11.2017.
 */

public class PhoneValidator extends StringValidator {
    @Override
    public String validate(String... args) {
        if (args[0].length() != 10)
            return App.getContext().getString(R.string.validation_incorrect_number);
        return null;
    }
}
