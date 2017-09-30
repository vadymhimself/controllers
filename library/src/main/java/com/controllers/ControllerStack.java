package com.controllers;

import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.*;

/**
 * Created by Vadym Ovcharenko
 * 18.10.2016.
 */

public class ControllerStack implements Serializable, Iterable<Controller> {

    private final Stack<Controller> controllers = new Stack<>();

    @Nullable
    Controller peek() {
        if (controllers.isEmpty()) {
            return null;
        }
        return controllers.peek();
    }

    public void add(Controller controller) {
        controllers.add(controller);
    }

    public int size() {
        return controllers.size();
    }

    AbstractController pop() {
        return controllers.pop();
    }

    @Override
    public Iterator<Controller> iterator() {
        // reverse order iterator
        return new Iterator<Controller>() {
            ListIterator<Controller> it = controllers.listIterator(controllers.size());

            @Override public boolean hasNext() {
                return it.hasPrevious();
            }

            @Override public Controller next() {
                return it.previous();
            }
        };
    }

    List<Controller> pop(int i) {
        if (i > controllers.size() || i < 1) throw new IllegalArgumentException();

        List<Controller> popped = new ArrayList<>();

        for (int j = 0; j < i; j++) {
            popped.add(controllers.pop());
        }

        return popped;
    }

    Controller peek(int i) {
        if (i > controllers.size()) throw new IllegalArgumentException();
        int j = 0;
        for (Controller controller : this) {
            if (j == i) return controller;
            j++;
        }
        return null;
    }
}
