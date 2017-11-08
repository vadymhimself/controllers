package ru.xfit.misc.adapters;

import java.util.List;

/**
 * Created by TESLA on 07.11.2017.
 */

public interface Filter<T> {
    List<T> filter(List<T> list);
}
