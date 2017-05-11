package com.cvvm;

import java.io.Serializable;

/**
 * Created by Vadim Ovcharenko
 * 01.11.2016.
 */

public interface Task<R> extends Serializable {
    R exec() throws Throwable;
    void cancel();
}
