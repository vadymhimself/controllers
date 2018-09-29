package com.controllers.misc.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

public class EndlessRecyclerView extends HackyRecyclerView {

  private OnScrollListener innerScrollListener = new OnScrollListener() {
    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
      super.onScrolled(recyclerView, dx, dy);

      if (loadMoreListener == null || recyclerView.getAdapter() == null) return;

      LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
      if (layoutManager == null) throw new IllegalStateException("LayoutManager must be set");

      int totalItemCount = layoutManager.getItemCount();
      int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
      int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
      if (!loadMoreListener.isLoading() && !loadMoreListener.hasLoadedAllItems()) {
        if (firstVisibleItemPosition >= 0
            && totalItemCount - lastVisibleItemPosition <= loadMoreListener.getThreshold()) {
          loadMoreListener.loadMore();
        }
      }
    }
  };

  private LoadMoreListener loadMoreListener;

  public EndlessRecyclerView(Context context) {
    super(context);
    init();
  }

  public EndlessRecyclerView(Context context,
      @Nullable AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public EndlessRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init();
  }

  private void init() {
    addOnScrollListener(innerScrollListener);
  }

  public void setLoadMoreListener(LoadMoreListener loadMoreListener) {
    this.loadMoreListener = loadMoreListener;
  }

  public interface LoadMoreListener {
    void loadMore();

    boolean isLoading();

    boolean hasLoadedAllItems();

    int getThreshold();
  }
}
