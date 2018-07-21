package com.controllers;

import android.support.annotation.GuardedBy;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 *
 * TODO: properly synchronize
 *
 * Created by Vadym Ovcharenko
 * 18.10.2016.
 */

public class RouterStack<T extends IController> implements Serializable, Iterable<T> {

    interface Transaction<T> {
        void commit();
        void rollBack();
        boolean isInTransaction();

        List<T> pop(int howMany);
        T pop();
        void add(T element);
    }

    interface TransactionBlock<T> {
        void run(Transaction<T> transaction);
    }

    @NonNull
    private ArrayList<T> stack = new ArrayList<>();
    private final Map<Object, T> index = new HashMap<>();

    @GuardedBy("this")
    private boolean inTransaction; // guarded by this

    @NonNull
    transient Router router; // late init, injected from the router

    RouterStack(@NonNull Router router) {
        this.router = router;
    }

    @Nullable
    T peek() {
        if (stack.isEmpty()) {
            return null;
        }
        return stack.get(stack.size() - 1);
    }

    T peek(int indexFromEnd) {
        if (indexFromEnd > stack.size()) throw new IllegalArgumentException();
        return stack.get(stack.size() - indexFromEnd - 1);
    }

    T get(int index) {
        return stack.get(index);
    }

    public int size() {
        return stack.size();
    }

    synchronized boolean isInTransaction() {
        return inTransaction;
    }

    synchronized void transaction(TransactionBlock<T> block) {
        StackTransaction transaction = new StackTransaction();
        transaction.begin();
        try {
            block.run(transaction);
        } catch (Throwable t) {
            // check if user rolled back manually
            if (transaction.isInTransaction()) {
                transaction.rollBack();
                throw new RuntimeException("Transaction failed with rollback", t);
            }
            throw t;
        }
    }

    @NonNull
    @Override
    public Iterator<T> iterator() {
        // reverse order iterator
        return new Iterator<T>() {
            ListIterator<T> it = stack.listIterator(stack.size());

            @Override public boolean hasNext() {
                return it.hasPrevious();
            }

            @Override public T next() {
                return it.previous();
            }
        };
    }

    private class StackTransaction implements Transaction<T> {

        private ArrayList<T> stackBackup;

        private void begin() {
            synchronized (RouterStack.this) {
                checkNotInTransaction();
                this.stackBackup = stack;
                stack = new ArrayList<>(stackBackup);
                inTransaction = true;
            }
        }

        @Override
        public void commit() {
            synchronized (RouterStack.this) {
                checkInTransaction();
                notifyControllerStackChanges();
                stackBackup = null;
                inTransaction = false;
            }
        }

        private void notifyControllerStackChanges() {
            // use stack backup and the new stack to notify changed states
            for (T element : stack) {
                if (!stackBackup.remove(element)) { // pop all remained elements
                    // was not in the old list, so it's new
                    element.onAttachedToStack(router);
                }
            }

            // by this time all remained elements were removed
            for (T removed : stackBackup) {
                // only removed left
                removed.onDetachedFromStack(router);
            }
        }

        @Override
        public void rollBack() {
            synchronized (RouterStack.this) {
                checkInTransaction();
                stack = stackBackup;
                stackBackup = null;
                inTransaction = false;
            }
        }

        @Override
        public boolean isInTransaction() {
            return RouterStack.this.isInTransaction();
        }

        @Override
        public List<T> pop(int howMany) {
            checkInTransaction();
            if (howMany > stack.size() || howMany < 1) throw new IllegalArgumentException();

            List<T> popped = new ArrayList<>();

            for (int j = 0; j < howMany; j++) {
              popped.add(pop());
            }

            return popped;
        }

        @Override
        public T pop() {
            checkInTransaction();
            T c = stack.remove(stack.size() - 1);
            index.remove(c.hashCode());
            return c;
        }

        @Override
        public void add(T element) {
            checkInTransaction();
            T c = index.put(element.hashCode(), element);
            if (c != null) {
              throw new IllegalArgumentException("Element already exists in the index");
            }
            stack.add(element);
        }

        private void checkInTransaction() {
            if (!inTransaction || stackBackup == null) {
                throw new IllegalStateException("Transaction has been finished or never started");
            }
        }

        private void checkNotInTransaction() {
            if (inTransaction || stackBackup != null) {
                throw new IllegalStateException("There is a transaction in progress already");
            }
        }
    }

}
