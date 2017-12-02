package ru.xfit.misc.views.maskededittext;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by aleks on 02.12.2017.
 */

public class DisabledPasteMaskedEditText extends MaskedEditText {
    public DisabledPasteMaskedEditText(Context context) {
        super(context);
    }

    public DisabledPasteMaskedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DisabledPasteMaskedEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public int getSelectionStart() {
        for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
            if (element.getMethodName().equals("canPaste")) {
                return -1;
            }
        }
        return super.getSelectionStart();
    }
}
