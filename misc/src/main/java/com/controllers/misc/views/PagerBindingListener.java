package com.controllers.misc.views;

import android.support.v4.view.ViewPager;

/**
 * Binds to another viewpager for synchronous scrolling
 */
public class PagerBindingListener implements ViewPager.OnPageChangeListener {

    private final ViewPager pager;
    private int index = 0;

    public PagerBindingListener(ViewPager pager) {
        this.pager = pager;
    }

    @Override
    public void onPageSelected(int position) {
        index = position;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int
            positionOffsetPixels) {
        int width = pager.getWidth();
        pager.scrollTo((int) (width * position + width * positionOffset), 0);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            pager.setCurrentItem(index);
        }
    }
}
