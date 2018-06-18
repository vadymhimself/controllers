package com.controllers.misc;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

public class ScrollingBehavior extends CoordinatorLayout.Behavior<View> implements
        AppBarLayout.OnOffsetChangedListener {

    private int nestedScrollOffsetY;
    private int ablOffsetY;

    private View child;

    public ScrollingBehavior() {
    }

    public ScrollingBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onMeasureChild(CoordinatorLayout parent, View child,
                                  int parentWidthMeasureSpec, int widthUsed,
                                  int parentHeightMeasureSpec, int heightUsed) {
        this.child = child;
        return super.onMeasureChild(parent, child, parentWidthMeasureSpec,
                widthUsed, parentHeightMeasureSpec, heightUsed);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                       @NonNull View child, @NonNull View directTargetChild,
                                       @NonNull View target, int axes, int type) {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View
            dependency) {
        if (dependency instanceof AppBarLayout) {
            AppBarLayout abl = (AppBarLayout) dependency;
            abl.removeOnOffsetChangedListener(this);
            abl.addOnOffsetChangedListener(this);
            return true;
        }
        return false;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (Math.abs(verticalOffset) < appBarLayout.getTotalScrollRange()) {
            // if appbar expanded, reset nested scroll
            nestedScrollOffsetY = 0;
        }
        ablOffsetY = verticalOffset;
        if (child != null) {
            child.setTranslationY(-nestedScrollOffsetY + ablOffsetY);
        }
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                               @NonNull View child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        nestedScrollOffsetY += dyConsumed;
        child.setTranslationY(-nestedScrollOffsetY + ablOffsetY);
    }
}
