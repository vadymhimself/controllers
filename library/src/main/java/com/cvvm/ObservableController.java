package com.cvvm;

import android.databinding.ViewDataBinding;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

abstract class ObservableController<B extends ViewDataBinding> extends SerializableController<B> {

    public interface Observer extends Serializable {
        void onAttachedToStack(ObservableController observable);
        void onDetachedFromStack(ObservableController observable);
        void onAttachedToScreen(ObservableController observable);
        void onDetachedFromScreen(ObservableController observable);
    }

    private Set<Observer> observers;

    ObservableController() {
    }

    boolean addObserver(Observer observer) {
        if (observers == null) {
            observers = new HashSet<>();
        }
        return observers.add(observer);
    }

    boolean removeObserver(Observer observer) {
        return observers != null && observers.remove(observer);
    }

    @Override void onAttachedToStack() {
        super.onAttachedToStack();
        if (observers != null) {
            for (Observer observer : observers) {
                observer.onAttachedToStack(this);
            }
        }
    }

    @Override void onDetachedFromStack() {
        super.onDetachedFromStack();
        if (observers != null) {
            for (Observer observer : observers) {
                observer.onDetachedFromStack(this);
            }
        }
    }

    @Override void onAttachedToScreen() {
        super.onAttachedToScreen();
        if (observers != null) {
            for (Observer observer : observers) {
                observer.onAttachedToScreen(this);
            }
        }
    }

    @Override void onDetachedFromScreen() {
        super.onDetachedFromScreen();
        if (observers != null) {
            for (Observer observer : observers) {
                observer.onDetachedFromScreen(this);
            }
        }
    }
}
