package ru.xfit.screens;

import android.databinding.ViewDataBinding;
import com.controllers.Controller;
import ru.xfit.domain.App;

public abstract class XFitController<B extends ViewDataBinding> extends Controller<B> {

    private String title;

    @Override
    protected void onAttachedToStack() {
        App.getBus().register(this);
    }

    @Override
    protected void onDetachedFromStack() {
        App.getBus().unregister(this);
    }

    protected void  setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

//    @Override
//    protected void onRestored(boolean deserialized) {
//        if (deserialized) {
//            App.getBus().register(this);
//        }
//    }
}
