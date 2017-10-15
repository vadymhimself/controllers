package ru.xfit.misc.views;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;

import java.io.Serializable;

/**
 * Created by User on 13.07.2017.
 */

public class MyAppBarLayout extends AppBarLayout
        implements AppBarLayout.OnOffsetChangedListener {

    public static final String STATE = "state";
    private State state;
    private OnStateChangeListener onStateChangeListener;

    public MyAppBarLayout(Context context) {
        super(context);
    }

    public MyAppBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!(getLayoutParams() instanceof CoordinatorLayout.LayoutParams)
                || !(getParent() instanceof CoordinatorLayout)) {
            throw new IllegalStateException(
                    "MyAppBarLayout must be a direct child of CoordinatorLayout.");
        }
        addOnOffsetChangedListener(this);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset == 0) {
            if (onStateChangeListener != null && state != State.EXPANDED) {
                onStateChangeListener.onStateChange(State.EXPANDED);
            }
            state = State.EXPANDED;
        } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
            if (onStateChangeListener != null && state != State.COLLAPSED) {
                onStateChangeListener.onStateChange(State.COLLAPSED);
            }
            state = State.COLLAPSED;
        } else {
            if (onStateChangeListener != null && state != State.IDLE) {
                onStateChangeListener.onStateChange(State.IDLE);
            }
            state = State.IDLE;
        }
    }

    public void setOnStateChangeListener(OnStateChangeListener listener) {
        this.onStateChangeListener = listener;
    }

    public interface OnStateChangeListener {
        void onStateChange(State toolbarChange);
    }

    public enum State implements Serializable {
        COLLAPSED,
        EXPANDED,
        IDLE
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("superState", super.onSaveInstanceState());
        bundle.putSerializable(STATE, state);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            State myState = (State) bundle.getSerializable(STATE);
            this.state = myState;
            if (!(myState == State.EXPANDED)) {
                setExpanded(false);
            }
            state = bundle.getParcelable("superState");
        }
        super.onRestoreInstanceState(state);
    }
}
