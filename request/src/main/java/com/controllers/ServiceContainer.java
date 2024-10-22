package com.controllers;

import android.os.Parcelable;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Vadym Ovcharenko
 * 21.10.2016.
 */

final class ServiceContainer {

    private static final Map<Class, ? super Object> map = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <S> void registerService(final S s) {
        if (s.getClass().getInterfaces().length == 0) {
            throw new IllegalArgumentException("Service instance must implement an interface");
        }

        final Class clazz = s.getClass().getInterfaces()[0];

        S proxy = (S) Proxy.newProxyInstance(clazz.getClassLoader(), new
                        Class<?>[]{clazz},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method,
                                         Object[] args)
                            throws Throwable {
                        // If the method is a method from Object then defer
                        // to normal invocation.
                        if (method.getDeclaringClass() == Object.class) {
                            return method.invoke(this, args);
                        }
                        Object actual = method.invoke(s, args);
                        if (actual instanceof Task) {
                            // check if parameters are serializable
                            for (Class paramType : method.getParameterTypes()) {
                                if (!paramType.isPrimitive() &&
                                        !Serializable.class
                                                .isAssignableFrom(paramType) &&
                                        !Parcelable.class
                                                .isAssignableFrom(paramType)) {
                                    throw new IllegalStateException("Service " +
                                            "method parameters must be " +
                                            "serializable: " + paramType.getName());
                                }
                            }
                            // create serializable task adapter
                            return new TaskSerializableAdapter<>(
                                    (Task<?>) actual,
                                    clazz,
                                    method.getName(),
                                    method.getParameterTypes(),
                                    args
                            );
                        }
                        return actual;
                    }
                });

        map.put(clazz, proxy);
    }

    @SuppressWarnings("unchecked")
    static <I> I getService(Class<I> clazz) {
        return (I) map.get(clazz);
    }
}
