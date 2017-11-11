package ru.xfit.misc.adapters;

import android.databinding.ListChangeRegistry;
import android.databinding.ObservableList;
import android.support.annotation.MainThread;
import android.support.v7.util.DiffUtil;
import android.support.v7.util.ListUpdateCallback;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by TESLA on 09.11.2017.
 */

public class DiffObservableList<T> extends AbstractList<T> implements ObservableList<T> {

    private final Object LIST_LOCK = new Object();
    private List<T> list = Collections.emptyList();
    private final Callback<T> callback;
    private final boolean detectMoves;
    private final ListChangeRegistry listeners = new ListChangeRegistry();
    private final ObservableListUpdateCallback listCallback = new ObservableListUpdateCallback();

    public DiffObservableList(Callback<T> callback) {
        this(callback, true);
    }

    public DiffObservableList(Callback<T> callback, boolean detectMoves) {
        this.callback = callback;
        this.detectMoves = detectMoves;
    }

    public DiffUtil.DiffResult calculateDiff(final List<T> newItems) {
        final ArrayList<T> frozenList;
        synchronized (LIST_LOCK) {
            frozenList = new ArrayList<>(list);
        }
        return doCalculateDiff(frozenList, newItems);
    }

    private DiffUtil.DiffResult doCalculateDiff(final List<T> oldItems, final List<T> newItems) {
        return DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return oldItems.size();
            }

            @Override
            public int getNewListSize() {
                return newItems != null ? newItems.size() : 0;
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                T oldItem = oldItems.get(oldItemPosition);
                T newItem = newItems.get(newItemPosition);
                return callback.areItemsTheSame(oldItem, newItem);
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                T oldItem = oldItems.get(oldItemPosition);
                T newItem = newItems.get(newItemPosition);
                return callback.areContentsTheSame(oldItem, newItem);
            }
        }, detectMoves);
    }

    @MainThread
    public void update(List<T> newItems, DiffUtil.DiffResult diffResult) {
        synchronized (LIST_LOCK) {
            list = newItems;
        }
        diffResult.dispatchUpdatesTo(listCallback);
    }

    @MainThread
    public void update(List<T> newItems) {
        DiffUtil.DiffResult diffResult = doCalculateDiff(list, newItems);
        list = newItems;
        diffResult.dispatchUpdatesTo(listCallback);
    }

    @Override
    public void addOnListChangedCallback(OnListChangedCallback<? extends ObservableList<T>> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeOnListChangedCallback(OnListChangedCallback<? extends ObservableList<T>> listener) {
        listeners.remove(listener);
    }

    @Override
    public T get(int i) {
        return list.get(i);
    }

    @Override
    public int size() {
        return list.size();
    }

    public interface Callback<T> {

        boolean areItemsTheSame(T oldItem, T newItem);

        boolean areContentsTheSame(T oldItem, T newItem);
    }

    class ObservableListUpdateCallback implements ListUpdateCallback {

        @Override
        public void onChanged(int position, int count, Object payload) {
            listeners.notifyChanged(DiffObservableList.this, position, count);
        }

        @Override
        public void onInserted(int position, int count) {
            modCount += 1;
            listeners.notifyInserted(DiffObservableList.this, position, count);
        }

        @Override
        public void onRemoved(int position, int count) {
            modCount += 1;
            listeners.notifyRemoved(DiffObservableList.this, position, count);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            listeners.notifyMoved(DiffObservableList.this, fromPosition, toPosition, 1);
        }
    }
}
