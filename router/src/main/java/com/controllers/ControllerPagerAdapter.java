package com.controllers;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vadym Ovcharenko
 * 04.11.2016.
 */

public class ControllerPagerAdapter implements DelegatingPagerAdapter.Delegate,
        Serializable {

    private List<Controller> controllerList = new ArrayList<>();
    @NonNull private final AbstractController parent;

    transient private DelegatingPagerAdapter pagerAdapter;

    public ControllerPagerAdapter(@NonNull final ObservableController parent) {
        this.parent = parent;
        // controllers inside the viewpager do not receive lifecycle signals,
        // so we have to push it manually from the parent controller
        parent.addObserver(new ParentControllerObserver());
    }

    @Override
    public Controller getItem(int position) {
        return controllerList.get(position);
    }

    public void addController(Controller controller) {
        if (controllerList.add(controller)) {
            if (parent.isAttachedToStack() && !controller.isAttachedToStack()) {
                controller.onAttachedToStackInternal(parent.getActivity());
            }
            notifyChange();
        }
    }

    public void removeController(Controller controller) {
        if (controllerList.remove(controller)) {
            controller.onDetachedFromStackInternal();
            notifyChange();
        }
    }

    @Deprecated
    public void set(int index, Controller controller) {
        controllerList.set(index, controller);
        notifyChange();
    }

    private void notifyChange() {
        if (pagerAdapter != null && parent.isAttachedToScreen() &&
                parent.getActivity() != null && !parent.asFragment()
                .getChildFragmentManager().isDestroyed()) {
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

    public PagerAdapter asFragmentPagerAdapter() {
        if (parent.getActivity() == null) {
            throw new IllegalStateException("Parent controller is not " +
                    "attached yet.");
        }
        pagerAdapter = new DelegatingPagerAdapter(parent.asFragment()
                .getChildFragmentManager(), this);
        return pagerAdapter;
    }

    public boolean contains(Controller controller) {
        return controllerList.contains(controller);
    }

    private final class ParentControllerObserver implements
            ObservableController.Observer {
        @Override
        public void onAttachedToStack(@NonNull ObservableController observable) {
            for (Controller controller : controllerList) {
                controller.onAttachedToStackInternal(parent.getActivity());
            }
        }

        @Override
        public void onDetachedFromStack(@NonNull ObservableController observable) {
            for (Controller controller : controllerList) {
                controller.onDetachedFromStackInternal();
            }
        }

        @Override
        public void onAttachedToScreen(@NonNull ObservableController observable) {
            // propagated via #instantiateItem
        }

        @Override
        public void onDetachedFromScreen(@NonNull ObservableController observable) {
            for (Controller controller : controllerList) {
                // forward signal of the parent detach
                if (controller.isAttachedToScreen()) {
                    controller.onDetachedFromScreenInternal();
                }
            }
        }

        @Override
        public void onRestored(@NonNull ObservableController observable) {

        }
    }
}
