package ru.xfit.screens.clubs;

import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;

import com.controllers.Request;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.xfit.R;
import ru.xfit.databinding.LayoutClubsBinding;
import ru.xfit.domain.App;
import ru.xfit.misc.adapters.BaseAdapter;
import ru.xfit.misc.adapters.BaseVM;
import ru.xfit.misc.views.MessageDialog;
import ru.xfit.model.data.ErrorCodes;
import ru.xfit.model.data.ErrorResponse;
import ru.xfit.model.data.club.ClubItem;
import ru.xfit.model.retrorequest.NetworkError;
import ru.xfit.model.service.Api;
import ru.xfit.screens.DrawerController;
import ru.xfit.screens.xfit.MyXfitController;

/**
 * Created by TESLA on 06.11.2017.
 */

public class ClubsController extends DrawerController<LayoutClubsBinding> implements MessageDialog.DialogResultListener {

    @Bindable
    public final BaseAdapter<BaseVM> adapter = new BaseAdapter<>(new ArrayList<>());

    public final ObservableBoolean progress = new ObservableBoolean();
    private boolean fromMyXfit;

    public ClubsController(boolean fromMyXfit) {
        progress.set(true);
        this.fromMyXfit = fromMyXfit;
        Request.with(this, Api.class)
                .create(Api::getClubs)
                .onFinally(() -> progress.set(false))
                .execute(this::addClubs);
    }


    public void addClubs(List<ClubItem> clubs) {
        List<BaseVM> toAdd = new ArrayList<>();
        String city = "";
        Collections.sort(clubs, (clubItem1, clubItem2) -> clubItem1.city.compareTo(clubItem2.city));
        for (ClubItem club : clubs) {
            if (!city.equals(club.city)) {
                toAdd.add(new CityVM(club.city));
                city = club.city;
            }
            if (club.id.equals("181")) {
                toAdd.add(1, new ClubVM(club, this, true, fromMyXfit));
            } else
                toAdd.add(new ClubVM(club, this, false, fromMyXfit));
        }
        adapter.addAll(toAdd);
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_clubs;
    }

    @Override
    public String getTitle() {
        return App.getContext().getString(R.string.clubs_controller_title);
    }

    public void linkToClub(String clubId) {
        progress.set(true);
        Request.with(this, Api.class)
                .create(api -> api.linkToClub(clubId))
                .onError(e -> {
                    if (e instanceof NetworkError) {
                        showMessage(((NetworkError) e).getErrorResponse());
                    } else {
                        showMessage(e.getMessage());
                    }
                })
                .onFinally(() -> progress.set(false))
                .execute(result -> {
                    //contracts
                    if (getPrevious() instanceof MyXfitController) {
                        ((MyXfitController) getPrevious()).setContractList(result);
                        back();
                    }

                });
    }

    private void showMessage(String message) {
        MessageDialog messageDialog = new MessageDialog.Builder()
                .setMessage(message)
                .build();

        messageDialog.setController(this);
        messageDialog.show(getActivity().getSupportFragmentManager(), "MY_TAG");
    }

    private void showMessage(ErrorResponse errorResponse) {
        MessageDialog messageDialog;

        if (errorResponse.code.equals(ErrorCodes.NO_LINKED_CLUBS)) {
            messageDialog = new MessageDialog.Builder()
                    .setMessage(App.getContext().getString(R.string.dialog_phone_not_found))
                    .setNegativeText(R.string.dialog_cancell)
                    .setPositiveText(R.string.dialog_buy_card)
                    .build();
        } else {
            messageDialog = new MessageDialog.Builder()
                    .setMessage(errorResponse.message)
                    .build();
        }

        messageDialog.setController(this);
        messageDialog.show(getActivity().getSupportFragmentManager(), "MY_TAG");
    }

    @Override
    public void onPositive(@NonNull String tag) {
        //buy card

    }

    @Override
    public void onNegative(@NonNull String tag) {

    }
}
