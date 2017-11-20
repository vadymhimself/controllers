package ru.xfit.screens.xfit;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.controllers.Request;

import java.util.List;

import ru.xfit.R;
import ru.xfit.databinding.LayoutMyxfitBinding;
import ru.xfit.domain.App;
import ru.xfit.model.data.contract.Contract;
import ru.xfit.model.service.Api;
import ru.xfit.screens.DrawerController;

/**
 * Created by TESLA on 20.11.2017.
 */

public class MyXfitController extends DrawerController<LayoutMyxfitBinding> {
    public ObservableField<Contract> contract;
    public final ObservableBoolean progress = new ObservableBoolean();

    public MyXfitController() {
        progress.set(true);
        Request.with(this, Api.class)
                .create(Api::getContracts)
                .onFinally(() -> progress.set(false))
                .execute(this::getMyClub);
    }

    private void getMyClub(List<Contract> contractList) {
        if (contractList != null && contractList.size() > 0)
            contract = new ObservableField<>(contractList.get(0));
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_myxfit;
    }

    @Override
    public String getTitle() {
        return App.getContext().getResources().getString(R.string.my_xfit_title);
    }
}
