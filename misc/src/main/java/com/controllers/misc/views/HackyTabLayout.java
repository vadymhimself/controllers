package android.support.design.widget; // fake location to override package default methods

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

/**
 * Created by User on 23.12.2017.
 */

public class HackyTabLayout extends TabLayout {

    public interface OnTabSelectedListener extends TabLayout.OnTabSelectedListener {
        boolean beforeTabSelected(Tab tab);
    }

    public static final String SELECTED = "selected_item_tabl";

    private OnTabSelectedListener listener;

    public HackyTabLayout(Context context) {
        super(context);
    }

    public HackyTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HackyTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void addOnTabSelectedListener(@NonNull TabLayout.OnTabSelectedListener listener) {
        if (listener instanceof OnTabSelectedListener) {
            this.listener = (OnTabSelectedListener) listener;
        }
        super.addOnTabSelectedListener(listener);
    }

    @Override
    void selectTab(final Tab tab, boolean updateIndicator) {
        if (listener != null && listener.beforeTabSelected(tab)) {
            return;
        }
        super.selectTab(tab, updateIndicator);
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
