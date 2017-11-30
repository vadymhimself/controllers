package ru.xfit.screens.clubs.news;

import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.view.MenuItem;

import com.controllers.Request;

import java.util.ArrayList;
import java.util.List;

import ru.xfit.R;
import ru.xfit.databinding.LayoutNewsBinding;
import ru.xfit.misc.adapters.BaseAdapter;
import ru.xfit.misc.adapters.BaseVM;
import ru.xfit.model.data.news.News;
import ru.xfit.model.service.Api;
import ru.xfit.screens.BlankToolbarController;

/**
 * Created by aleks on 29.11.2017.
 */

public class NewsController extends BlankToolbarController<LayoutNewsBinding> {

    @Bindable
    public final BaseAdapter<BaseVM> adapter = new BaseAdapter<>(new ArrayList<>());
    public ObservableBoolean progress = new ObservableBoolean();
    private String id;

    public NewsController(String id) {
        this.id = id;
        loadClubs();
    }

    private void loadClubs() {
        progress.set(true);
        Request.with(this, Api.class)
                .create(api -> api.getClubNews(id))
                .onFinally(() -> progress.set(false))
                .execute(this::initNews);
    }

    private void initNews(List<News> news) {
        List<BaseVM> newsVMs = new ArrayList<>();
        for (News item : news) {
            newsVMs.add(new NewsVM(item, this));
        }
        adapter.addAll(newsVMs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_news;
    }

    @Override
    public String getTitle() {
        return getActivity() == null ? "" : getActivity().getString(R.string.clubs_news);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    @Override
    public void onNavigationClick() {
        back();
    }
}
