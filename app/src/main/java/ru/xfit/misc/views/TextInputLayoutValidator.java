package ru.xfit.misc.views;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import ru.xfit.misc.utils.validation.EmailValidator;
import ru.xfit.misc.utils.validation.EmptyValidator;
import ru.xfit.misc.utils.validation.PasswordValidator;
import ru.xfit.misc.utils.validation.PhoneValidator;
import ru.xfit.misc.utils.validation.StringValidator;
import ru.xfit.misc.utils.validation.ValidationType;

/**
 * Created by aleks on 03.12.2017.
 */

public class TextInputLayoutValidator implements View.OnFocusChangeListener, TextWatcher {
    private ObservableField<String> errorText;
    private ObservableBoolean isErrorShown;
    private EmptyValidator validator;
    private int focusTrig;

    public TextInputLayoutValidator(ObservableField<String> errorText, ValidationType validationType, ObservableBoolean isErrorShown) {
        this.errorText = errorText;
        this.isErrorShown = isErrorShown;
        switch (validationType) {
            case TEXT:
                validator = new StringValidator();
                break;
            case EMAIL:
                validator = new EmailValidator();
                break;
            case PASSWORD:
                validator = new PasswordValidator();
                break;
            case PHONE:
                validator = new PhoneValidator();
                break;
            default:
                validator = new EmptyValidator();
        }
        updateValue("");
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (focusTrig == 1)
            isErrorShown.set(true);
        focusTrig++;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        updateValue(s.toString());
    }

    protected void updateValue(String newValue) {
        errorText.set(validator.validate(newValue));
    }
}
