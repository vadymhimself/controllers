package com.controllers.misc.views;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;

/**
 * Created by aleks on 23.02.2018.
 */

public abstract class ContentScrimAppBarListener implements AppBarLayout.OnOffsetChangedListener {

    public enum State {
        SCRIMMED,
        UNSCRIMMED
    }

    private State mCurrentState = State.UNSCRIMMED;

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        //TODO rewrite using behaviour
        int scrimTriggerHeight = ((CollapsingToolbarLayout)appBarLayout.getChildAt(0)).getScrimVisibleHeightTrigger();
        if (appBarLayout.getHeight() - Math.abs(i) <= scrimTriggerHeight){
            if (mCurrentState != State.SCRIMMED) {
                onStateChanged(appBarLayout, State.SCRIMMED);
            }
            mCurrentState = State.SCRIMMED;
        } else {
            if (mCurrentState != State.UNSCRIMMED) {
                onStateChanged(appBarLayout, State.UNSCRIMMED);
            }
            mCurrentState = State.UNSCRIMMED;
        }
    }

    public abstract void onStateChanged(AppBarLayout appBarLayout, State state);
}
