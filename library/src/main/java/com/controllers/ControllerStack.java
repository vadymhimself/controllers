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
    private final Map<Object, Controller> index = new HashMap<>();

    @Nullable
    Controller peek() {
        if (controllers.isEmpty()) {
            return null;
        }
        return controllers.peek();
    }

    public void add(Controller controller) {
        Controller c = index.put(controller.getTag(), controller);
        if (c != null) {
            throw new IllegalArgumentException("Controller with this tag " +
                    "already exists in the index");
        }
        controllers.add(controller);
    }

    AbstractController pop() {
        Controller c = controllers.pop();
        index.remove(c.getTag());
        return c;
    }

    List<Controller> pop(int i) {
        if (i > controllers.size() || i < 1) throw new IllegalArgumentException();

        List<Controller> popped = new ArrayList<>();

        for (int j = 0; j < i; j++) {
            Controller c = controllers.pop();
            index.remove(c);
            popped.add(c);
        }

        return popped;
    }

    Controller peek(int indexFromEnd) {
        if (indexFromEnd > controllers.size()) throw new IllegalArgumentException();
        return controllers.get(controllers.size() - indexFromEnd - 1);
    }

    Controller get(int index) {
        return controllers.get(index);
    }

    public int size() {
        return controllers.size();
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
}
