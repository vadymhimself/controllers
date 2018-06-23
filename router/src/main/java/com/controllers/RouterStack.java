package com.controllers;

import android.support.annotation.GuardedBy;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
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

public class RouterStack<T> implements Serializable, Iterable<T> {

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

    void beginTransaction(TransactionBlock<T> block) {
        StackTransaction transaction = new StackTransaction();
        transaction.begin();
        try {
            block.run(transaction);
        } catch (Throwable t) {
            if (transaction.isInTransaction()) {
                // TODO logger
                Log.e(getClass().getSimpleName(), "Uncaught exception inside a running "
                    + "transaction block, rolling back...");
                transaction.rollBack();
            }
            throw t;
        }
    }

    /**
     * @deprecated use beginTransaction() to perform mutating operations to the stack
     */
    @Deprecated
    void add(T element) {
        T c = index.put(element.hashCode(), element);
        if (c != null) {
            throw new IllegalArgumentException("Element already exists in the index");
        }
        stack.add(element);
    }

    /**
     * @deprecated use beginTransaction() to perform mutating operations to the stack
     */
    @Deprecated
    T pop() {
        T c = stack.remove(stack.size() - 1);
        index.remove(c.hashCode());
        return c;
    }

    /**
     * @deprecated use beginTransaction() to perform mutating operations to the stack
     */
    @Deprecated
    List<T> pop(int howMany) {
        if (howMany > stack.size() || howMany < 1) throw new IllegalArgumentException();

        List<T> popped = new ArrayList<>();

        for (int j = 0; j < howMany; j++) {
            popped.add(pop());
        }

        return popped;
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

        @Nullable
        private ArrayList<T> stackBackup;

        private synchronized void begin() {
            checkNotInTransaction();
            this.stackBackup = stack;
            stack = new ArrayList<>(stackBackup);
            inTransaction = true;
        }

        @Override
        public synchronized void commit() {
            checkInTransaction();
            stackBackup = null;
            inTransaction = false;
        }

        @Override
        public synchronized void rollBack() {
            checkInTransaction();
            stack = stackBackup;
            stackBackup = null;
            inTransaction = false;
        }

        @Override public boolean isInTransaction() {
            return RouterStack.this.isInTransaction();
        }

        @Override
        public List<T> pop(int howMany) {
            checkInTransaction();
            return RouterStack.this.pop(howMany);
        }

        @Override
        public T pop() {
            checkInTransaction();
            return RouterStack.this.pop();
        }

        @Override
        public void add(T element) {
            checkInTransaction();
            RouterStack.this.add(element);
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
