package com.cvvm;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.io.Serializable;

/**
 * Delegates call to the delegate
 * Created by Vadim Ovcharenko
 * 04.11.2016.
 */

class DelegatedPagerAdapter extends FragmentStatePagerAdapter implements Serializable {

    private final Delegate delegate;

    DelegatedPagerAdapter(FragmentManager fm, Delegate delegate) {
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

    interface Delegate {
        Controller getItem(int position);
        int getCount();
        CharSequence getPageTitle(int position);
    }
}
