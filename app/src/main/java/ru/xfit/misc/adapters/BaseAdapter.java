package ru.xfit.misc.adapters;

import android.databinding.Bindable;
import android.databinding.Observable;
import android.databinding.PropertyChangeRegistry;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import ru.xfit.BR;

public class BaseAdapter<VM extends BaseVM> extends RecyclerView.Adapter<BindingHolder<ViewDataBinding, VM>> implements Serializable, Iterable<VM>, Observable {
    List<VM> vms;

    private transient PropertyChangeRegistry registry = new PropertyChangeRegistry();

    public BaseAdapter(@NonNull List<VM> vms) {
        this.vms = vms;
    }

    @Override
    public int getItemViewType(int position) {
        return vms.get(position).getLayoutId();
    }

    @Override
    public BindingHolder<ViewDataBinding, VM> onCreateViewHolder(ViewGroup p, @LayoutRes int viewType) {
        return BindingHolder.create(LayoutInflater.from(p.getContext()), viewType, p);
    }

    @Override
    public void onBindViewHolder(BindingHolder<ViewDataBinding, VM> holder, int position) {
        holder.bindViewModel(vms.get(position));
        holder.binding.executePendingBindings();
    }

    @Bindable
    public boolean isAdapterEmpty() {
        return this.getItemCount() == 0;
    }

    @Override
    public int getItemCount() {
        return vms.size();
    }

    public void add(VM item) {
        vms.add(item);
        notifyItemInserted(vms.size());
    }

    public void add(int position, VM item) {
        vms.add(position, item);
        notifyItemInserted(position);
        registry.notifyChange(this, BR.adapterEmpty);
    }

    public void addAll(List<VM> list) {
        int previousLength = list.size();
        vms.addAll(list);
        notifyItemRangeInserted(previousLength, list.size());
        registry.notifyChange(this, BR.adapterEmpty);
    }

    public void addAll(List<VM> list, int position){
        vms.addAll(position, list);
        notifyItemRangeInserted(position, list.size());
        registry.notifyChange(this, BR.adapterEmpty);
    }

    public void remove(VM vm) {
        int index = vms.indexOf(vm);
        if (index != -1) {
            vms.remove(index);
            notifyItemRemoved(index);
            registry.notifyChange(this, BR.adapterEmpty);
        }
    }

    public VM remove(int index) {
        if (index != -1) {
            VM vm = vms.remove(index);
            if (vm != null) {
                notifyItemRemoved(index);
                registry.notifyChange(this, BR.adapterEmpty);
            }
            return vm;
        }
        return null;
    }

    public List<VM> getItems() {
        return vms;
    }

    public VM getItem(int index) {
        return vms.get(index);
    }

    public int indexOf(VM o) {
        return vms.indexOf(o);
    }

    public void clear() {
        vms.clear();
        notifyDataSetChanged();
    }

    @Override public Iterator<VM> iterator() {
        return vms.iterator();
    }

    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback onPropertyChangedCallback) {
        registry.add(onPropertyChangedCallback);
    }

    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback onPropertyChangedCallback) {
        registry.remove(onPropertyChangedCallback);
    }
}
