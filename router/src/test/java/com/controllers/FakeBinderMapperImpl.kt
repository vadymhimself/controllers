package android.databinding

import android.view.View
import com.controllers.LifecycleDataBinderMapper

class DataBinderMapperImpl : MergedDataBinderMapper() {

  init {
    addMapper(LifecycleDataBinderMapper())
  }

  override fun getDataBinder(
    bindingComponent: DataBindingComponent?,
    view: View?,
    layoutId: Int
  ): ViewDataBinding  {
    return super.getDataBinder(bindingComponent, view, layoutId) ?: FakeViewDataBinding(bindingComponent, view, 0)
  }
}

internal class FakeViewDataBinding(
  component: DataBindingComponent?,
  val rootView: View?,
  localFieldCount: Int
) : ViewDataBinding(component, rootView, localFieldCount) {

  init {
    setRootTag(rootView)
  }

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