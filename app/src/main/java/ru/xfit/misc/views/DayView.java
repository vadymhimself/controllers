package ru.xfit.misc.views;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ru.xfit.misc.utils.UiUtils;

/**
 * Created by TESLA on 31.10.2017.
 */

public class DayView extends RelativeLayout {
    private Date mDate;
    private Day day;
    private TextView textView;

    public DayView(Context context) {
        this(context, null, 0);
    }

    public DayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        textView = new TextView(context);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14.5f);

        RelativeLayout.LayoutParams lp = new LayoutParams(UiUtils.dpToPx(40), UiUtils.dpToPx(40));
        lp.addRule(CENTER_IN_PARENT, RelativeLayout.TRUE);
        textView.setLayoutParams(lp);
        textView.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        textView.setGravity(Gravity.CENTER);

        this.addView(textView);
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

    public void setText(String text) {
        textView.setText(text);
    }

    public void setTextColor(int color) {
        textView.setTextColor(color);
    }

    public void setTextBg(int resId) {
        textView.setBackgroundResource(resId);
    }

    public void setBold() {
        textView.setTypeface(null, Typeface.BOLD);
    }

    public void setDay(Day day) {
        setText(String.valueOf(day.getDay()));
        this.day = day;
        invalidate();
    }

    public Day getDay() {
        return day;
    }
}
