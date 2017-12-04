package ru.xfit.misc.views;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import ru.xfit.misc.utils.validation.ValidationType;

/**
 * Created by aleks on 04.12.2017.
 */

public class CompareTextInputLayoutValidator extends TextInputLayoutValidator {

    private String compareValue;
    private String value;

    public CompareTextInputLayoutValidator(ObservableField<String> errorText, ValidationType validationType, ObservableBoolean isErrorShown) {
        super(errorText, validationType, isErrorShown);
    }


    public void setCompareValue(String compareValue) {
        this.compareValue = compareValue;
        updateValue();
    }

    @Override
    protected void updateValue(String newValue) {
        value = newValue;
        if (compareValue == null)
            return;
        errorText.set(validator.validate(newValue, compareValue));
    }

    private void updateValue() {
        if (compareValue == null)
            return;
        errorText.set(validator.validate(value, compareValue));
    }
}
