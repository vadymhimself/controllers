package ru.xfit.screens.clubs.news;

import android.view.View;

import com.controllers.Controller;

import ru.xfit.R;
import ru.xfit.misc.adapters.BaseVM;
import ru.xfit.model.data.news.News;

/**
 * Created by aleks on 29.11.2017.
 */

public class NewsVM implements BaseVM {

    public News news;
    public Controller controller;

    public NewsVM(News news, Controller controller) {
        this.news = news;
        this.controller = controller;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_news;
    }

    public void onItemClicked(View view) {
        controller.show(new AboutNewsController(news));
    }
}
