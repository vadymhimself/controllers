package com.controllers.misc.adapters.binding;

import android.databinding.BindingAdapter;
import android.text.TextWatcher;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

/**
 * Created by Artem Sisetskyi on 6/20/18.
 * AppDevelopmentShop
 * sisetskyi.a@gmail.com
 */
public abstract class EditTextAdapters {

    @BindingAdapter("actionSearchListener")
    public static void _bindSearchListener(EditText view,
                                          final OnSearchListener onSearchListener) {
        view.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                onSearchListener.onSearchQuery(v.getText().toString());
                return true;
            }
            return false;
        });
    }

    public interface OnSearchListener {
        void onSearchQuery(String text);
    }

    @BindingAdapter("textWatcher")
    public static void _bindTextWatcher(EditText editText, TextWatcher textWatcher) {
        if (editText != null) {
            editText.addTextChangedListener(textWatcher);
        }
    }

}
