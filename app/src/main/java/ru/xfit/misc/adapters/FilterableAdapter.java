package ru.xfit.misc.adapters;

import android.support.annotation.NonNull;
import ru.xfit.misc.utils.ListUtils;
import ru.xfit.screens.clubs.TrainerFilter;

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

    public void replaceFilter(Filter<VM> oldFilter, Filter<VM> newFilter) {
        if (filters.contains(oldFilter)) {
            int oldIndex = filters.indexOf(oldFilter);
            filters.remove(oldIndex);
            filters.add(oldIndex, newFilter);
            refresh();
        }
    }

    public void clearFilters() {
        filters.clear();
        vms = allVms;
        refresh();
    }

    public void refresh() {
        vms = ListUtils.reduce(filters, allVms, (reduced, current, i, c) -> current.filter(reduced));
        notifyDataSetChanged(); // TODO: diff util
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

    public boolean isEmpty() {
        return vms.isEmpty();
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
