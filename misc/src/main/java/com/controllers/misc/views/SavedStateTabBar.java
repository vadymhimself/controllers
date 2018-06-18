package com.controllers.misc.views;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;

/**
 * Created by User on 23.12.2017.
 */

public class SavedStateTabBar extends TabLayout {

    public static final String SELECTED = "selected_item_tabl";

    public SavedStateTabBar(Context context) {
        super(context);
    }

    public SavedStateTabBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SavedStateTabBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("superState", super.onSaveInstanceState());
        bundle.putInt(SELECTED, getSelectedTabPosition());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            int id = bundle.getInt(SELECTED);
            getTabAt(id).select();
            state = bundle.getParcelable("superState");
        }
        super.onRestoreInstanceState(state);
    }
}
