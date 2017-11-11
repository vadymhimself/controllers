package ru.xfit.misc.adapters;

import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

public class BaseAdapter<VM extends BaseVM> extends RecyclerView.Adapter<BindingHolder<ViewDataBinding, VM>> implements Serializable, Iterable<VM> {
    List<VM> vms;

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
    }

    public void addAll(List<VM> list) {
        int previousLength = list.size();
        vms.addAll(list);
        notifyItemRangeInserted(previousLength, list.size());
    }

    public void addAll(List<VM> list, int position){
        vms.addAll(position, list);
        notifyItemRangeInserted(position, list.size());
    }

    public void remove(VM vm) {
        int index = vms.indexOf(vm);
        if (index != -1) {
            vms.remove(index);
            notifyItemRemoved(index);
        }
    }

    public VM remove(int index) {
        if (index != -1) {
            VM vm = vms.remove(index);
            if (vm != null) notifyItemRemoved(index);
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
}
