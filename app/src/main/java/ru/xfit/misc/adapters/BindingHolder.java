package ru.xfit.misc.adapters;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import ru.xfit.BR;


/**
 * Created by Vadim Ovcharenko
 * 22.10.2016.
 */

public class BindingHolder<T extends ViewDataBinding, VM> extends RecyclerView.ViewHolder {

    private static final String TAG = BindingHolder.class.getSimpleName();
    public final T binding;

    private BindingHolder(T binding) {
        super(binding.getRoot());
        this.binding = DataBindingUtil.bind(itemView);
    }

    public void bindViewModel(VM vm) {
        binding.setVariable(BR.viewModel, vm);
    }

    public static <T extends ViewDataBinding, VM>
    BindingHolder<T, VM> create(LayoutInflater layoutInflater, @LayoutRes int resId, ViewGroup p) {
        try {
            return new BindingHolder<>(DataBindingUtil.inflate(layoutInflater, resId, p, false));
        } catch (Throwable e) {
            String resName = layoutInflater.getContext().getResources().getResourceName(resId);
            Log.e(TAG, "Error inflating layout " + resName);
            throw e;
        }
    }
}