package ru.xfit.misc.views;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

/**
 * Created by aleks on 07.12.2017.
 */

public class DisabledPasteEditText extends AppCompatEditText {
    public DisabledPasteEditText(Context context) {
        super(context);
    }

    public DisabledPasteEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DisabledPasteEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
