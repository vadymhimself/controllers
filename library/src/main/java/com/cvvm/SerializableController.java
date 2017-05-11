package com.cvvm;

import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * TAG is generated once per creation of the controller.
 * TODO: possible ConcurrentModificationException on promise Collection?
 */

abstract class SerializableController<B extends ViewDataBinding> extends AbstractController<B> implements Serializable {

    private String tag = UUID.randomUUID().toString();
    private Map<Object, PromiseInternal> promises = new HashMap<>();

    @NonNull @Override public final String getTag() {
        return tag;
    }

    @Override public final void attachPromise(PromiseInternal promise) {
        promises.put(promise.getTask().hashCode(), promise);
    }

    @Override public final void detachPromise(PromiseInternal promise) {
        promises.remove(promise.getTask().hashCode());
    }

    @Override void onRestored() {
        super.onRestored();
        for (PromiseInternal promise : promises.values()) {
            if (!promise.hasTerminated()) {
                promise.enqueue();
            }
        }
    }

    @Override void onDetached() {
        super.onDetached();
        for (PromiseInternal promise : promises.values()) {
            if (!promise.hasTerminated()) {
                promise.cancel();
            }
        }
    }
}
