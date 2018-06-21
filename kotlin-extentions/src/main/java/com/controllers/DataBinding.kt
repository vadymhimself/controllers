package com.controllers

import android.databinding.BaseObservable
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class BindableProperty<in R : BaseObservable, T>(
        private val propBrId: Int,
        initializer: () -> T? = { null }
) : ReadWriteProperty<R, T?> {

    var field: T? = initializer()

    override fun setValue(thisRef: R, property: KProperty<*>, value: T?) {
        field = value
        thisRef.notifyPropertyChanged(propBrId)
    }

    override fun getValue(thisRef: R, property: KProperty<*>): T? {
        return field
    }

}