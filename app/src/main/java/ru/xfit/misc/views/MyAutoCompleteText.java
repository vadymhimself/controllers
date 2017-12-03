package ru.xfit.misc.views;

import android.content.Context;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.util.AttributeSet;

import ru.xfit.R;
import ru.xfit.domain.App;

/**
 * Created by aleks on 01.12.2017.
 */

public class MyAutoCompleteText extends AppCompatAutoCompleteTextView {

    public MyAutoCompleteText(Context context) {
        super(context);
        init();
    }

    public MyAutoCompleteText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyAutoCompleteText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setCompoundDrawablesWithIntrinsicBounds(null, null, App.getContext().getDrawable(R.drawable.ic_arrow_drop_down), null);
    }

    @Override
    public void dismissDropDown() {
        if (isPopupShowing())
            setCompoundDrawablesWithIntrinsicBounds(null, null, App.getContext().getDrawable(R.drawable.ic_arrow_drop_down), null);
        super.dismissDropDown();
    }

    @Override
    public void showDropDown() {
        if (!isPopupShowing())
            setCompoundDrawablesWithIntrinsicBounds(null, null, App.getContext().getDrawable(R.drawable.ic_arrow_drop_up), null);
        super.showDropDown();
    }
}
