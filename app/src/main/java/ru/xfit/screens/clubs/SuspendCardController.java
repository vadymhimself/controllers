package ru.xfit.screens.clubs;

import android.util.Log;

import org.joda.time.DateTime;

import ru.xfit.R;
import ru.xfit.databinding.LayoutSuspendCardBinding;
import ru.xfit.domain.App;
import ru.xfit.screens.DateChangeListener;
import ru.xfit.screens.XFitController;

/**
 * Created by TESLA on 17.11.2017.
 */

public class SuspendCardController extends XFitController<LayoutSuspendCardBinding> implements DateChangeListener {

    @Override
    public int getLayoutId() {
        return R.layout.layout_suspend_card;
    }

    @Override
    public String getTitle() {
        return App.getContext().getResources().getString(R.string.suspend_card_title);
    }

    @Override
    public void onDateChange(DateTime dateTime) {}

    @Override
    public void onDatePeriodChanged(DateTime firstSelection, DateTime lastSelection) {
        Log.e(" >>>>> ", firstSelection + " " + lastSelection);
    }
}
