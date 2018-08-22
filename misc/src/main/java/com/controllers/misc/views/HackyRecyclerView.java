package com.controllers.misc.views;

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

    public HackyRecyclerView(Context context) {
        this(context, null, 0);
    }

    public HackyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HackyRecyclerView(Context context, @Nullable AttributeSet attrs,
                             int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        if (getLayoutManager() != null) {
            return new SavedState(superState, getLayoutManager().onSaveInstanceState());
        }
        return superState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable != null && parcelable instanceof SavedState) {
            SavedState state = (SavedState) parcelable;
            super.onRestoreInstanceState(state.getSuperState());
            if (getLayoutManager() != null) {
                getLayoutManager().onRestoreInstanceState(state.lmState);
            }
        } else {
            super.onRestoreInstanceState(parcelable);
        }
    }

    static class SavedState extends BaseSavedState {
        Parcelable lmState;

        SavedState(Parcel in) {
            super(in);
            lmState = in.readParcelable(ClassLoader.getSystemClassLoader());
        }

        SavedState(Parcelable superState, Parcelable layoutManagerState) {
            super(superState);
            this.lmState = layoutManagerState;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeParcelable(lmState, flags);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
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