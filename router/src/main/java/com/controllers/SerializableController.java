package com.controllers;

import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import java.io.Serializable;
import java.util.UUID;

/**
 * TAG is generated once per creation of the controller.
 */

abstract class SerializableController<B extends ViewDataBinding> extends
        AbstractController<B> implements Serializable {

    private final String tag = UUID.randomUUID().toString();

    @NonNull
    final Object getTag() {
        return tag;
    }

}
