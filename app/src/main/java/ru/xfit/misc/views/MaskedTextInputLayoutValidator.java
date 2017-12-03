package ru.xfit.misc.views;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.text.Editable;
import android.view.View;

import ru.xfit.misc.utils.validation.ValidationType;
import ru.xfit.misc.views.maskededittext.MaskedEditText;

/**
 * Created by aleks on 03.12.2017.
 */

public class MaskedTextInputLayoutValidator extends TextInputLayoutValidator {

    private MaskedEditText maskedEditText;

    public MaskedTextInputLayoutValidator(ObservableField<String> errorText, ValidationType validationType, ObservableBoolean isErrorShown, MaskedEditText editText) {
        super(errorText, validationType, isErrorShown);
        this.maskedEditText = editText;
    }

    @Override
    public void afterTextChanged(Editable s) {
        updateValue(maskedEditText.getRawText());
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        super.onFocusChange(v, hasFocus);

    }
}
