package com.cvvm;

import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Stack;

/**
 * Created by Vadim Ovcharenko
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

    AbstractController pop(int i) {
        if (i > controllers.size()) throw new IllegalArgumentException();

        AbstractController controller = null;
        for (int j = 0; j < i; j++) {
            controller = controllers.pop();
        }
        return controller;
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
