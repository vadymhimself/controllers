package com.controllers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;

/**
 * Created by Vadym Ovcharenko
 * 28.10.2016.
 */

class TaskSerializableAdapter<T> implements Task<T> {

    private transient Task<T> actual;

    private Class service;
    private String method;
    private Class<?>[] parameterTypes;
    private Object[] args;

    TaskSerializableAdapter(Task<T> actual, Class service, String method,
                            Class<?>[] parameterTypes, Object[] args) {
        this.actual = actual;
        this.service = service;
        this.method = method;
        this.parameterTypes = parameterTypes;
        this.args = args;
    }

    @Override
    public T exec() throws Throwable {
        return actual.exec();
    }

    @Override
    public void cancel() {
        actual.cancel();
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(service);
        out.writeObject(method);
        out.writeObject(parameterTypes);
        out.writeObject(args);
    }

    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream in) throws Throwable {
        this.service = (Class) in.readObject();
        this.method = (String) in.readObject();
        this.parameterTypes = (Class<?>[]) in.readObject();
        this.args = (Object[]) in.readObject();

        Method method = service.getDeclaredMethod(this.method,
                this.parameterTypes);
        Object obj = ServiceContainer.getService(this.service);
        this.actual = (Task<T>) method.invoke(obj, this.args);
    }
}
