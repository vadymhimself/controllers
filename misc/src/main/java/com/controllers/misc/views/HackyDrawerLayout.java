package com.controllers.misc.views;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


public class HackyDrawerLayout extends DrawerLayout {

    public HackyDrawerLayout(Context context) {
        super(context);
    }

    public HackyDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HackyDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            View drawer = getChildAt(1);
            return !(getDrawerLockMode(drawer) == LOCK_MODE_LOCKED_OPEN &&
                    ev.getRawX() > drawer.getWidth()) &&
                    super.onInterceptTouchEvent(ev);
        } catch (NullPointerException e) {
            e.printStackTrace();
            return super.onInterceptTouchEvent(ev);
        }
    }
}
