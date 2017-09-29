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

public class ControllerPagerAdapter implements DelegatingPagerAdapter
        .Delegate, Serializable {

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
        controllerList.add(controller);
    }

    public void set(int index, Controller controller) {
        controllerList.set(index, controller);
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

    private final class ParentControllerObserver implements
            ObservableController.Observer {
        @Override
        public void onAttachedToStack(ObservableController observable) {
            for (Controller controller : controllerList) {
                controller.onAttachedToStackInternal();
            }
        }

        @Override
        public void onDetachedFromStack(ObservableController observable) {
            for (Controller controller : controllerList) {
                controller.onDetachedFromStackInternal();
            }
        }

        @Override
        public void onAttachedToScreen(ObservableController observable) {

        }

        @Override
        public void onDetachedFromScreen(ObservableController observable) {
            for (Controller controller : controllerList) {
                // forward signal of the parent detach
                if (controller.isAttachedToScreen()) {
                    controller.onDetachedFromScreen();
                }
            }
        }

        @Override
        public void onRestored(ObservableController observable) {

        }
    }
}
