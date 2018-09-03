package com.controllers

import java.io.Serializable
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class BindableProperty<in R : Controller<*>, T : Serializable>(
  private val propBrId: Int,
  private val lazyInitializer: suspend () -> T? = { null }
) : ReadWriteProperty<R, T?>, Serializable {

  var field: T? = null

  override fun setValue(
    thisRef: R,
    property: KProperty<*>,
    value: T?
  ) {
    field = value
    thisRef.notifyPropertyChanged(propBrId)
  }

  override fun getValue(
    thisRef: R,
    property: KProperty<*>
  ): T? {
    if (field == null) {
      thisRef.async {
        field = lazyInitializer()
        thisRef.notifyPropertyChanged(propBrId)
      }
    }
    return field
  }

}