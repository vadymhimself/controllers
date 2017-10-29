package ru.xfit.misc.utils.validation;

import ru.xfit.R;
import ru.xfit.domain.App;

/**
 * Created by TESLA on 27.10.2017.
 */

public class EmailValidator extends StringValidator {

    @Override
    public String validate(String... args) {
        String result = super.validate(args[0]);
        if (result != null)
            return result;
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(args[0]).matches())
            return App.getContext().getString(R.string.incorrect_email);
        return null;
    }
}
