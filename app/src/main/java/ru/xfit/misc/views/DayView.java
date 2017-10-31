package ru.xfit.misc.views;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by TESLA on 31.10.2017.
 */

public class DayView extends AppCompatTextView {
    private Date mDate;

    public DayView(Context context) {
        this(context, null, 0);
    }

    public DayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void bind(Date date) {
        this.mDate = date;

        final SimpleDateFormat df = new SimpleDateFormat("d", Locale.getDefault());
        int day = Integer.parseInt(df.format(date));
        setText(String.valueOf(day));
    }

    public Date getDate() {
        return mDate;
    }
}
