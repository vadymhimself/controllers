package ru.xfit.misc.adapters;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.util.Log;

import ru.xfit.misc.utils.ListUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FilterableAdapter<VM extends BaseVM> extends BaseAdapter<VM> {

    private List<VM> allVms;
    private final List<Filter<VM>> filters = new LinkedList<>();

    public FilterableAdapter(@NonNull List<VM> vms) {
        super(vms);
        allVms = new ArrayList<>(vms);
    }

    public void addFilter(Filter<VM> filter) {
        filters.add(filter);
        refresh();
    }

    public void clearFilters() {
        filters.clear();
        vms = allVms;
        refresh();
    }

    public void refresh() {
        vms.clear();
        vms.addAll(ListUtils.reduce(filters, allVms, (reduced, current, i, c) -> current.filter(reduced)));
        notifyDataSetChanged(); // TODO: diff util

//        DiffObservableList<VM> list = new DiffObservableList<VM>(new DiffObservableList.Callback<VM>() {
//            @Override
//            public boolean areItemsTheSame(VM oldItem, VM newItem) {
//                return oldItem.equals(newItem);
//            }
//
//            @Override
//            public boolean areContentsTheSame(VM oldItem, VM newItem) {
//                return oldItem.equals(newItem);
//            }
//        });
//
//        DiffUtil.DiffResult diffResult = list.calculateDiff(vms);
//        list.update(allVms);
//        list.update(vms, diffResult);
    }

    @Override
    public void add(VM item) {
        allVms.add(item);
        refresh();
    }

    @Override
    public void addAll(List<VM> list) {
        allVms.addAll(list);
        refresh();
    }

    @Override
    public void remove(VM vm) {
        allVms.remove(vm);
        refresh();
    }

    @Override
    public void clear() {
        allVms.clear();
        refresh();
    }

// TODO: implement

    @Override
    public VM remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int position, VM item) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addAll(List<VM> list, int position) {
        throw new UnsupportedOperationException();
    }

    public List<VM> getAllItems() {
        return allVms;
    }
}
