package ru.xfit.misc;


import android.support.v4.widget.SwipeRefreshLayout;

public interface RefreshListener {
    void onRefresh(SwipeRefreshLayout refreshLayout);
}