package com.cvvm;

import android.support.annotation.NonNull;

/**
 * Created by Vadim Ovcharenko
 * 21.10.2016.
 */

public interface Request<T> {

    Request<T> onError(Promise.ErrorAction errorAction);

    Request<T> onCanceled(Promise.Action cancelledAction);

    Request<T> onFinally(Promise.Action finallyAction);

    Promise<T> execute(@NonNull Promise.SuccessAction<T> successAction);

    Promise<T> execute();

}
