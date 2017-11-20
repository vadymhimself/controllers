package ru.xfit.screens.clubs;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.util.Log;
import android.view.View;

import com.controllers.Request;

import org.joda.time.DateTime;

import java.util.List;

import ru.xfit.R;
import ru.xfit.databinding.LayoutSuspendCardBinding;
import ru.xfit.domain.App;
import ru.xfit.model.data.contract.Contract;
import ru.xfit.model.data.contract.SuspendRequest;
import ru.xfit.model.service.Api;
import ru.xfit.screens.DateChangeListener;
import ru.xfit.screens.XFitController;

/**
 * Created by TESLA on 17.11.2017.
 */

public class SuspendCardController extends XFitController<LayoutSuspendCardBinding> implements DateChangeListener {

    public final ObservableBoolean progress = new ObservableBoolean();
    private final String clubId;
    private ObservableField<Contract> clubContract;
    private ObservableField<DateTime> firstDaySelection = new ObservableField<>();
    private ObservableField<DateTime> lastDaySelection = new ObservableField<>();
    public ObservableInt canSuspendDays = new ObservableInt();

    public SuspendCardController(String clubId) {
        this.clubId = clubId;

        progress.set(true);
        Request.with(this, Api.class)
                .create(Api::getContracts)
                .onFinally(() -> progress.set(false))
                .execute(this::searchCurrentContract);
    }

    private void searchCurrentContract(List<Contract> contractList) {
        for (Contract contract : contractList) {
            Log.e(">>>>", " " + contract.clubId);
            if (contract.clubId.equals(this.clubId)) {
                clubContract.set(contract);
                canSuspendDays.set(contract.canSuspendDays);
            }
        }
    }

    public void suspend(View view) {
        if (clubContract.get() != null) {
            if (firstDaySelection.get() == null || lastDaySelection.get() == null) {
                //pls select days
            } else {
                if (clubContract.get().canSuspend) {
                    progress.set(true);
                    SuspendRequest request = new SuspendRequest();
                    request.id = clubContract.get().id;
                    request.club = clubContract.get().clubId;
                    //2017-17-11
                    request.beginDate = firstDaySelection.get().toString("YYYY-d-M");
                    request.endDate = lastDaySelection.get().toString("YYYY-d-M");

                    Request.with(this, Api.class)
                            .create(api -> api.suspendContract(request))
                            .onFinally(() -> progress.set(false))
                            .onError(error -> {

                            })
                            .execute();
                } else {
                    //contract suspension is not possible
                }
            }
        } else {
            //contract not found
        }

    }

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
        firstDaySelection.set(firstSelection);
        lastDaySelection.set(lastSelection);
    }
}
