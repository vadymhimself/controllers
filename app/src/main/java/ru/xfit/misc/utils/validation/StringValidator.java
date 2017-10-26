package ru.xfit.misc.utils.validation;

import ru.xfit.R;
import ru.xfit.domain.App;

import static android.text.TextUtils.isEmpty;

/**
 * Created by TESLA on 27.10.2017.
 */

public class StringValidator  extends EmptyValidator {

    public String validate(String... args) {

        if (isEmpty(args[0]))
            return App.getContext().getString(R.string.empty_field_error);
        return null;

    }

}