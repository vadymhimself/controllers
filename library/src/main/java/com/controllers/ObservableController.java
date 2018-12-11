package com.controllers;

import android.databinding.ViewDataBinding;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Controller that notifies of its lifecycle. All observers are serialized.
 */
//this must be public to work with kotlin a really don`t know why
abstract public class ObservableController<B extends ViewDataBinding>
    extends SerializableController<B> {


    public interface Observer extends Serializable {
        void onAttachedToStack(ObservableController observable);
        void onDetachedFromStack(ObservableController observable);
        void onAttachedToScreen(ObservableController observable);
        void onDetachedFromScreen(ObservableController observable);
        void onRestored(ObservableController observable);
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

    @Override void onAttachedToStackInternal(ControllerActivity host) {
        super.onAttachedToStackInternal(host);
        if (observers != null) {
            for (Observer observer : new ArrayList<>(observers)) {
                observer.onAttachedToStack(this);
            }
        }
    }

    @Override void onDetachedFromStackInternal() {
        super.onDetachedFromStackInternal();
        if (observers != null) {
            for (Observer observer : new ArrayList<>(observers)) {
                observer.onDetachedFromStack(this);
            }
        }
    }

    @Override void onAttachedToScreenInternal() {
        super.onAttachedToScreenInternal();
        if (observers != null) {
            for (Observer observer : new ArrayList<>(observers)) {
                observer.onAttachedToScreen(this);
            }
        }
    }

    @Override void onDetachedFromScreenInternal() {
        super.onDetachedFromScreenInternal();
        if (observers != null) {
            for (Observer observer : new ArrayList<>(observers)) {
                observer.onDetachedFromScreen(this);
            }
        }
    }

    @Override
    void onRestoredInternal() {
        super.onRestoredInternal();
        if (observers != null) {
            for (Observer observer : new ArrayList<>(observers)) {
                observer.onRestored(this);
            }
        }
    }
}
