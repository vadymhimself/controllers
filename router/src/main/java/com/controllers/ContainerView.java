package com.controllers;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.controllers.core.RouterStack;
import com.controllers.core.ViewModel;

public class ContainerView<T extends ViewModel> extends FrameLayout {

  private RouterStack<T> stack;

  public ContainerView(@NonNull Context context) {
    super(context);
  }

  public ContainerView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public ContainerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public ContainerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  @Override
  protected Parcelable onSaveInstanceState() {
    Parcelable superState = super.onSaveInstanceState();
    return new SavedState(superState, stack);
  }

  @SuppressWarnings("unchecked")
  @Override
  protected void onRestoreInstanceState(Parcelable parcelable) {
    SavedState state = (SavedState) parcelable;
    super.onRestoreInstanceState(state.getSuperState());
    this.stack = state.stack;
  }

  RouterStack<T> getStack() {
    if (stack == null) {
      stack = new RouterStack<>();
    }
    return stack;
  }

  static class SavedState extends BaseSavedState {
    RouterStack stack;

    SavedState(Parcel in) {
      super(in);
      stack = (RouterStack) in.readSerializable();
    }

    SavedState(Parcelable superState, RouterStack stack) {
      super(superState);
      this.stack = stack;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
      super.writeToParcel(dest, flags);
      dest.writeSerializable(stack);
    }

    public static final Parcelable.Creator<SavedState> CREATOR
        = new Parcelable.Creator<SavedState>() {
      public SavedState createFromParcel(Parcel in) {
        return new SavedState(in);
      }

      public SavedState[] newArray(int size) {
        return new SavedState[size];
      }
    };
  }
}
