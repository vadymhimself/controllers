package com.cvvm;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.io.Serializable;

/**
 * Delegates call to the delegate
 * Created by Vadym Ovcharenko
 * 04.11.2016.
 */

class DelegatingPagerAdapter extends FragmentStatePagerAdapter implements Serializable {

    private final Delegate delegate;

    DelegatingPagerAdapter(FragmentManager fm, Delegate delegate) {
        super(fm);
        this.delegate = delegate;
    }

    @Override public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override public Fragment getItem(int position) {
        return delegate.getItem(position).asFragment();
    }

    @Override public int getCount() {
        return delegate.getCount();
    }

    @Override public CharSequence getPageTitle(int position) {
        return delegate.getPageTitle(position);
    }

    @Override public Parcelable saveState() {
        return null;
    }

    @Override public void restoreState(Parcelable state, ClassLoader loader) {
        // no need to restore state
    }

    @Override public Object instantiateItem(ViewGroup container, int position) {
        Object o =  super.instantiateItem(container, position);
        delegate.getItem(position).onAttachedToScreen();
        return o;
    }

    @Override public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        delegate.getItem(position).onDetachedFromScreen();
    }

    interface Delegate {
        Controller getItem(int position);
        int getCount();
        CharSequence getPageTitle(int position);
    }
}
