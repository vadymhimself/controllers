package com.cvvm;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vadim Ovcharenko
 * 04.11.2016.
 */

public class ControllerPagerAdapter implements DelegatedPagerAdapter.Delegate, Serializable {

    private List<Controller> controllerList = new ArrayList<>();
    @NonNull private final AbstractController parent;

    transient private DelegatedPagerAdapter pagerAdapter;

    public ControllerPagerAdapter(@NonNull AbstractController parent) {
        this.parent = parent;
    }

    @Override
    public Controller getItem(int position) {
        return controllerList.get(position);
    }

    public void addController(Controller controller) {
        controllerList.add(controller);
    }

    public void set(int index, Controller controller) {
        controllerList.set(index, controller);
        if (pagerAdapter != null && parent.isAttached() &&
                parent.getActivity() != null && !parent.asFragment().getChildFragmentManager().isDestroyed()) {
            pagerAdapter.notifyDataSetChanged(); // TODO: optimize
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return controllerList.get(position).getTitle();
    }

    @Override
    public int getCount() {
        return controllerList.size();
    }

    PagerAdapter asFragmentPagerAdapter() {
        if (parent.getActivity() == null) {
            throw new IllegalStateException("Parent controller is not attached yet.");
        }
        pagerAdapter = new DelegatedPagerAdapter(parent.asFragment().getChildFragmentManager(), this);
        return pagerAdapter;
    }
}
