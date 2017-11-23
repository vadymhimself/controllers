package ru.xfit.screens.xfit;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.util.Log;
import android.view.View;

import com.controllers.Request;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.List;

import ru.xfit.R;
import ru.xfit.databinding.LayoutMyxfitBinding;
import ru.xfit.domain.App;
import ru.xfit.model.data.club.ClubItem;
import ru.xfit.model.data.contract.Contract;
import ru.xfit.model.service.Api;
import ru.xfit.screens.DrawerController;
import ru.xfit.screens.clubs.AboutClubController;
import ru.xfit.screens.clubs.ClubsController;
import ru.xfit.screens.clubs.SuspendCardController;

/**
 * Created by TESLA on 20.11.2017.
 */

public class MyXfitController extends DrawerController<LayoutMyxfitBinding> {
    public ObservableField<Contract> contract = new ObservableField<>();
    public final ObservableBoolean progress = new ObservableBoolean();

    public ObservableField<String> contractStatus = new ObservableField<>();
    public ObservableField<String> contractDuration = new ObservableField<>();
    public ObservableField<ClubItem> contractClub = new ObservableField<>();
    public ObservableInt contractColor = new ObservableInt();

    public MyXfitController() {
        progress.set(true);
        Request.with(this, Api.class)
                .create(Api::getContracts)
                .onFinally(() -> progress.set(false))
                .execute(this::setContractList);
    }

    public void setContractList(List<Contract> contractList) {
        if (contractList != null && contractList.size() > 0) {
            //TODO replace to my club
            contract.set(contractList.get(0));

            /*  CONTRACT_OPENED(1),
                CONTRACT_CLOSED(2),
                CONTRACT_NOT_OPENED(3),
                CONTRACT_BLOCKED(4),
                CONTRACT_SUSPEND(5);*/

            switch (contract.get().status) {
                case 1:
                    contractStatus.set(App.getContext().getResources().getString(R.string.contract_opened));
                    contractColor.set(App.getContext().getResources().getColor(R.color.contractOpened));
                    break;
                case 2:
                    contractStatus.set(App.getContext().getResources().getString(R.string.contract_closed));
                    contractColor.set(App.getContext().getResources().getColor(R.color.contractClosed));
                    break;
                case 0:
                    contractStatus.set(App.getContext().getResources().getString(R.string.contract_not_opened));
                    contractColor.set(App.getContext().getResources().getColor(R.color.contractNotOpened));
                    break;
                case 3:
                    contractStatus.set(App.getContext().getResources().getString(R.string.contract_blocked));
                    contractColor.set(App.getContext().getResources().getColor(R.color.contractBlocked));
                    break;
                case 5:
                    contractStatus.set(App.getContext().getResources().getString(R.string.contract_suspended));
                    contractColor.set(App.getContext().getResources().getColor(R.color.contractSuspended));
                    break;
                default:
                    break;
            }

            DateTime endDate = DateTime.parse(contract.get().endDate,
                    DateTimeFormat.forPattern("yyyy-M-d"));
            contractDuration.set(App.getContext().getResources()
                    .getString(R.string.my_xfit_contract_duration, endDate.toString("d MMMM yyyy")));

            contractClub.set(contract.get().club);
        }
    }

    public void reloadContract() {
        progress.set(true);
        Request.with(this, Api.class)
                .create(Api::getContracts)
                .onFinally(() -> progress.set(false))
                .execute(this::setContractList);
    }


    @Override
    public int getLayoutId() {
        return R.layout.layout_myxfit;
    }

    @Override
    public String getTitle() {
        return App.getContext().getResources().getString(R.string.my_xfit_title);
    }

    public void linkToClub(View view) {
        show(new ClubsController(true));
    }

    public void onMyClubClick(View view) {
        if (contractClub.get() != null)
            show(new AboutClubController(contractClub.get(), true));
    }

    public void onSuspendCardClick(View view) {
        if (contract.get() != null)
            show(new SuspendCardController(contract.get()));
    }
}
