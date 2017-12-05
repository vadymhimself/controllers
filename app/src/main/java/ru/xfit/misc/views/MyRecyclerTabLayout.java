package ru.xfit.misc.views;

import android.content.Context;
import android.util.AttributeSet;

import com.nshmura.recyclertablayout.RecyclerTabLayout;

/**
 * Created by TESLA on 06.12.2017.
 */

public class MyRecyclerTabLayout extends RecyclerTabLayout {

    public MyRecyclerTabLayout(Context context) {
        super(context);
    }

    public MyRecyclerTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRecyclerTabLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void update(){
        mAdapter.notifyDataSetChanged();
    }
}
