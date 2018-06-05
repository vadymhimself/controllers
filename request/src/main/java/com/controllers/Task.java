package com.controllers;

import java.io.Serializable;

/**
 * Created by Vadym Ovcharenko
 * 01.11.2016.
 */

public interface Task<R> extends Serializable {
    R exec() throws Throwable;
    void cancel();
}
