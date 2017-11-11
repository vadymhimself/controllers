package ru.xfit.misc.adapters;

import ru.xfit.misc.utils.ListUtils;

import java.util.List;

public abstract class PredicateFilter<T> implements Filter<T> {

    @Override
    public final List<T> filter(List<T> list) {
        return ListUtils.filter(list, this::call);
    }

    protected abstract boolean call(T it);
}
