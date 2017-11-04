package ru.xfit.misc;

public interface LoadMoreListener {
    void loadMore();
    boolean isLoading();
    boolean hasLoadedAllItems();
}
