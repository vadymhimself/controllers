package ru.xfit.misc.views;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MenuItem;

public class BottomNavigationView extends android.support.design.widget.BottomNavigationView {

    private static final String SELECTED_ITEM = "_slctd_item";

    public BottomNavigationView(Context context) {
        super(context);
    }

    public BottomNavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("superState", super.onSaveInstanceState());
        bundle.putInt(SELECTED_ITEM, getSelectedItemId());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            int id = bundle.getInt(SELECTED_ITEM);
            if (id != 0) {
                getMenu().findItem(id).setChecked(true);
            }
            state = bundle.getParcelable("superState");
        }
        super.onRestoreInstanceState(state);
    }

    public int getSelectedItemId() {
        for (int i = 0, size = getMenu().size(); i < size; i++) {
            MenuItem menuItem = getMenu().getItem(i);
            if (menuItem.isChecked()) return menuItem.getItemId();
        }
        return 0;
    }
}
