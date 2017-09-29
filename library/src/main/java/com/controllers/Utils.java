package com.controllers;

import android.util.Pair;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Utils {

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
}
