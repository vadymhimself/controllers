package ru.xfit.misc.views;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;


/**
 * Because the original recycler leaks adapter
 * Created by Vadym Ovcharenko
 * 27.10.2016.
 */

public class HackyRecyclerView extends RecyclerView {

    //not perfect solution but o cant find in fucking
    // RecyclerView method to get current scroll Y position
    private int mCurrentYPosition;

    private OnScrollListener innerScrollListener = new OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            mCurrentYPosition += dy;
        }
    };

    public HackyRecyclerView(Context context) {
        super(context);
        init();
    }

    public HackyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HackyRecyclerView(Context context, @Nullable AttributeSet attrs,
                             int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        addOnScrollListener(innerScrollListener);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (getAdapter() != null) {
            setAdapter(null);
        }
        removeOnScrollListener(innerScrollListener);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState newState = new SavedState(superState);
        newState.mScrollPosition = mCurrentYPosition;
        return newState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        if (state != null && state instanceof SavedState) {
            int scrollPosition = ((SavedState) state).mScrollPosition;
            this.postDelayed(() -> scrollBy(0, scrollPosition), 50);
        }
    }

    static class SavedState extends BaseSavedState {
        int mScrollPosition;

        SavedState(Parcel in) {
            super(in);
            mScrollPosition = in.readInt();
        }

        SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(mScrollPosition);
        }

        public static final Creator<SavedState> CREATOR
                = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}