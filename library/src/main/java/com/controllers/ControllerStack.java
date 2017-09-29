package com.controllers;

import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.*;

/**
 * Created by Vadym Ovcharenko
 * 18.10.2016.
 */

public class ControllerStack implements Serializable, Iterable<AbstractController> {

    private final Stack<AbstractController> controllers = new Stack<>();

    @Nullable
    AbstractController peek() {
        if (controllers.isEmpty()) {
            return null;
        }
        return controllers.peek();
    }

    public void add(AbstractController controller) {
        controllers.add(controller);
    }

    public int size() {
        return controllers.size();
    }

    AbstractController pop() {
        return controllers.pop();
    }

    @Override
    public Iterator<AbstractController> iterator() {
        // reverse order iterator
        return new Iterator<AbstractController>() {
            ListIterator<AbstractController> it = controllers.listIterator(controllers.size());

            @Override public boolean hasNext() {
                return it.hasPrevious();
            }

            @Override public AbstractController next() {
                return it.previous();
            }
        };
    }

    List<AbstractController> pop(int i) {
        if (i > controllers.size() || i < 1) throw new IllegalArgumentException();

        List<AbstractController> popped = new ArrayList<>();

        for (int j = 0; j < i; j++) {
            popped.add(controllers.pop());
        }

        return popped;
    }

    AbstractController peek(int i) {
        if (i > controllers.size()) throw new IllegalArgumentException();
        int j = 0;
        for (AbstractController controller : this) {
            if (j == i) return controller;
            j++;
        }
        return null;
    }
}
