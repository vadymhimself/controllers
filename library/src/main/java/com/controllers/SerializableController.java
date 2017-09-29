package com.controllers;

import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * TAG is generated once per creation of the controller.
 */

abstract class SerializableController<B extends ViewDataBinding> extends
        AbstractController<B> implements Serializable {

    private String tag = UUID.randomUUID().toString();

    @NonNull
    @Override
    public final String getTag() {
        return tag;
    }

}
