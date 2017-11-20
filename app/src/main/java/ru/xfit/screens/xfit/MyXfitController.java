package ru.xfit.screens.xfit;

import android.databinding.ObservableField;

import ru.xfit.R;
import ru.xfit.databinding.LayoutMyxfitBinding;
import ru.xfit.model.data.contract.Contract;
import ru.xfit.screens.DrawerController;

/**
 * Created by TESLA on 20.11.2017.
 */

public class MyXfitController extends DrawerController<LayoutMyxfitBinding> {
    public ObservableField<Contract> contract;

    @Override
    public int getLayoutId() {
        return R.layout.layout_myxfit;
    }
}
