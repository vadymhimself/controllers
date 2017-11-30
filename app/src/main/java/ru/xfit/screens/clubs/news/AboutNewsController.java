package ru.xfit.screens.clubs.news;

import android.view.MenuItem;

import ru.xfit.R;
import ru.xfit.databinding.LayoutAboutNewsBinding;
import ru.xfit.misc.TransparentStatusBar;
import ru.xfit.model.data.news.News;
import ru.xfit.screens.BlankToolbarController;

/**
 * Created by aleks on 30.11.2017.
 */

public class AboutNewsController extends BlankToolbarController<LayoutAboutNewsBinding> implements TransparentStatusBar {

    public News news;

    public AboutNewsController(News news) {
        this.news = news;

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    @Override
    public void onNavigationClick() {
        back();
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_about_news;
    }
}
