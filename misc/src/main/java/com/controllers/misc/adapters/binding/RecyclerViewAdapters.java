package com.controllers.misc.adapters.binding;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import com.controllers.misc.views.EndlessRecyclerView;
import com.controllers.misc.views.RecyclerItemClickListener;

/**
 * Created by Artem Sisetskyi on 6/20/18.
 * AppDevelopmentShop
 * sisetskyi.a@gmail.com
 */
public abstract class RecyclerViewAdapters {

    @BindingAdapter("layoutChangeListener")
    public static void _bindLayoutChangeListener(RecyclerView recyclerView, View.OnLayoutChangeListener listener) {
        if (listener != null) {
            recyclerView.addOnLayoutChangeListener(listener);
        }
    }

    @BindingAdapter("itemClickListener")
    public static void _bindItemClickListener(RecyclerView recyclerView, RecyclerItemClickListener.OnItemClickListener listener) {
        if (listener != null) {
            recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(recyclerView.getContext(), listener));
        }
    }

    @BindingAdapter("nestedScrollingEnabled")
    public static void _bindNestScrollEnabled(RecyclerView rv, Boolean enabled) {
        // Horizontal RW prevents CollapsingToolbarLayout from being collapsed
        // https://code.google.com/p/android/issues/detail?id=177613
        // http://stackoverflow.com/questions/30894334/android-design-lib-collapsingtoolbarlayout-stops-interact-when-horizontal-recycl
        rv.setNestedScrollingEnabled(enabled);
    }

    @BindingAdapter("itemSwipeListener")
    public static void _bindItemSwipeListener(RecyclerView recyclerView, final ItemSwipeListener swipeListener) {
        if (swipeListener == null) return;

        ItemTouchHelper.SimpleCallback cb = new ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                swipeListener.onItemSwiped(viewHolder.getAdapterPosition());
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(cb);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @BindingAdapter("loadMoreListener")
    public static void _bindLoadMoreListener(EndlessRecyclerView recyclerView,
        EndlessRecyclerView.LoadMoreListener listener) {
        recyclerView.setLoadMoreListener(listener);
    }

    public interface ItemSwipeListener {
        void onItemSwiped(int index);
    }
}
