package ru.xfit.misc.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rx.functions.Action1;
import rx.functions.Action2;

/**
 * Created by Vadim Ovcharenko
 * 13.06.16.
 */
public class ListUtils {

    /**
     * Filters collection to list saving order.
     * @return new list containing only instances that matches predicate or empty list.
     */
    public static <T> List<T> filter(Collection<T> collection, Predicate<T> predicate) {
        List<T> result = new ArrayList<>(collection.size()/2);
        for (T it : collection) {
            if (predicate.call(it)) result.add(it);
        }
        return result;
    }

    public static <T,V> void reduce(@NonNull Collection<T> collection, @NonNull Reducer<T,V> reducer) {
        reduce(collection, null, reducer);
    }

    /**
     * The reduce() method applies a function against an accumulator and each value of the array (from left-to-right) to reduce it to a single value.
     */
    public static <T, V> V reduce(@NonNull Collection<T> collection, @Nullable V startValue, @NonNull Reducer<T,V> reducer) {
        Iterator<T> iterator = collection.iterator();
        V reduced = startValue;
        for (int i = 0; iterator.hasNext(); i++) {
            T it = iterator.next();
            reduced = reducer.call(reduced, it, i, collection);
        }
        return reduced;
    }

    /**
     * Sublist that does not throws IndexOutOfBoundsException
     */
    public static <T> List<T> subList(List<T> items, int start, int end) {
        return items.subList(start < 0 ? 0 : start, end > items.size() ? items.size() : end);
    }

    public interface Reducer<T, V> {
        V call(V reduced, T current, int index, Collection<T> collection);
    }

    /**
     * Maps each object in input collection to a new list using given mapper. Return null from mapper
     * if don't want object to be included in the output list.
     * @return new list of mapped objects, or empty
     */
    public static <T, V> List<V> map(Collection<T> collection, Mapper<T,V> mapper) {
        List<V> result = new ArrayList<>(collection.size());
        for (T it : collection) {
            V mapped = mapper.map(it);
            if (mapped != null) result.add(mapped);
        }
        return result;
    }

    public static <T, V> List<V> map(T[] collection, Mapper<T,V> mapper) {
        List<V> result = new ArrayList<>(collection.length);
        for (T it: collection) {
            V mapped = mapper.map(it);
            if (mapped != null) result.add(mapped);
        }
        return result;
    }

    public static <K, V, T> List<T> map(Map<K, V> collection, MapMapper<K, V, T> mapper) {
        List<T> result = new ArrayList<>(collection.size());
        for (Map.Entry<K, V> it : collection.entrySet()) {
            T mapped = mapper.map(it);
            if (mapped != null) result.add(mapped);
        }
        return result;
    }

    /**
     * Collects each object in input collection to a new HashSet using given mapper. Return null from mapper
     * if don't want object to be included in the output list.
     * @return new list of mapped objects, or empty
     */
    public static <T, V> Set<V> collect(Collection<T> collection, Mapper<T,V> mapper) {
        Set<V> result = new HashSet<>(collection.size());
        for (T it : collection) {
            V mapped = mapper.map(it);
            if (mapped != null) result.add(mapped);
        }
        return result;
    }

    /**
     * @param <F> input type
     * @param <T> output type
     */
    public interface Mapper<F,T> {
        /**
         * Maps object of input type to object of output type
         *
         * @return returns mapped object or null if wanted to be ignored
         */
        T map(F it);
    }

    public interface MapMapper<K, V, T> {
        /**
         * Maps object of input type to object of output type
         *
         * @return returns mapped object or null if wanted to be ignored
         */
        T map(Map.Entry<K, V> it);
    }

    /**
     * Combines collections into a new list which is a result of concatenation of the all collections.
     */
    @SafeVarargs
    public static <T> List<T> concat(Collection<T>... collections) {
        List<T> merged = new LinkedList<>(collections[0]);
        for (int i = 1; i < collections.length; i++) {
            merged.addAll(collections[i]);
        }
        return merged;
    }

    /**
     * Combines collections into a new list of unique items
     * which is a result of distinct concatenation of collections saving order.
     */
    @SafeVarargs
    public static <T> Set<T> concatDistinct(Collection<T>... collections) {
        Set<T> merged = new LinkedHashSet<>(collections[0]);
        for (int i = 1; i < collections.length; i++) {
            merged.addAll(collections[i]);
        }
        return merged;
    }

    public static <T> Collection<T> forEach(Collection<T> collection, Action1<T> action) {
        for (T it : collection) action.call(it);
        return collection;
    }

    public static <T> Iterable<T> forEach(Iterable<T> collection, Action2<T, Integer> action) {
        Iterator<T> it = collection.iterator();
        for (int i = 0; it.hasNext(); i++) {
            action.call(it.next(), i);
        }
        return collection;
    }

    public static <T> Collection<T> forFirst(Collection<T> collection, int count, Action1<T> action) {
        Iterator<T> it = collection.iterator();
        int i = 0;
        while (it.hasNext() && i++ < count) {
            action.call(it.next());
        }
        return collection;
    }

    public static <T> List<T> crop(List<T> list, int count) {
        if (count >= list.size()) {
            return list;
        }
        return list.subList(0, count);
    }

    public static <K, T> Map<K, T> toMap(Collection<T> values, Mapper<T, K> mapper) {
        Map<K, T> map = new HashMap<>(values.size());
        for (T value : values) {
            map.put(mapper.map(value), value);
        }
        return map;
    }

    public static <T, V> List<Pair<T,V>> zip(Iterable<T> first, Iterable<V> second) {
        List<Pair<T, V>> list = new ArrayList<>();
        Iterator<T> it1 = first.iterator();
        Iterator<V> it2 = second.iterator();
        while (it1.hasNext() || it2.hasNext()) {
            T t = it1.hasNext() ? it1.next() : null;
            V v = it2.hasNext() ? it2.next() : null;
            list.add(new Pair<>(t,v));
        }
        return list;
    }

    public static List<Integer> toIntList(int[] array) {
        List<Integer> list = new ArrayList<>();
        for (int i : array) list.add(i);
        return list;
    }

    /**
     * @return first item in collection that matches predicate
     */
    public static <T> T first(Iterable<T> collection, Predicate<T> predicate) {
        for (T t : collection) {
            if (predicate.call(t)) return t;
        }
        return null;
    }
    /**
     * @return index of first item in collection that matches predicate
     */
    public static <T> int indexOfFirst(Iterable<T> collection, Predicate<T> predicate) {
        int i = 0;
        for (T t : collection) {
            if (predicate.call(t)) return i;
            i++;
        }
        return -1;
    }

    public static <T> boolean isEmptyOrNull(Collection<T> collection) {
        return collection == null || collection.isEmpty();
    }

    public interface Predicate<T> {
        boolean call(T t);
    }
}