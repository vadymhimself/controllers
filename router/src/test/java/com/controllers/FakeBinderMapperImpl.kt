package android.databinding

import android.view.View

class DataBinderMapperImpl : MergedDataBinderMapper() {

  override fun getDataBinder(
    bindingComponent: DataBindingComponent?,
    view: View?,
    layoutId: Int
  ): ViewDataBinding = FakeViewDataBinding(bindingComponent, view, 0)
}

internal class FakeViewDataBinding(
  component: DataBindingComponent?,
  view: View?,
  localFieldCount: Int
) : ViewDataBinding(component, view, localFieldCount) {

  override fun executeBindings() {
  }

  override fun onFieldChange(
    localFieldId: Int,
    `object`: Any?,
    fieldId: Int
  ): Boolean {
    return false
  }

  override fun invalidateAll() {
  }

  override fun hasPendingBindings() = false

  override fun setVariable(variableId: Int, value: Any?) = false

}