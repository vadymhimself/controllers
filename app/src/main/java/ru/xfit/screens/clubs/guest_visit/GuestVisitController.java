package ru.xfit.screens.clubs.guest_visit;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.view.MenuItem;
import android.view.View;

import com.controllers.Request;

import java.util.List;

import ru.xfit.R;
import ru.xfit.databinding.LayoutGuestVisitBinding;
import ru.xfit.domain.App;
import ru.xfit.misc.utils.DataUtils;
import ru.xfit.model.data.club.ClubItem;
import ru.xfit.model.data.club.SortingRequest;
import ru.xfit.model.data.storage.preferences.PreferencesStorage;
import ru.xfit.model.service.Api;
import ru.xfit.screens.BlankToolbarController;

/**
 * Created by aleks on 01.12.2017.
 */

public class GuestVisitController extends BlankToolbarController<LayoutGuestVisitBinding> {

    public final ObservableBoolean progress = new ObservableBoolean();
    public ObservableArrayList<ClubItem> clubs = new ObservableArrayList<>();
    public ObservableField<String> selectedClub = new ObservableField<>();
    public ObservableField<String> phone = new ObservableField<>("");
    public ObservableField<String> name = new ObservableField<>("");

    public GuestVisitController(ClubItem clubItem) {
        this.selectedClub.set(clubItem.title);
        PreferencesStorage storage = new PreferencesStorage(App.getContext());
        name.set(storage.getCurrentUser().name);
        phone.set(storage.getCurrentUser().phone);
        progress.set(true);
        Request.with(this, Api.class)
                .create(Api::getClubs)
                .onFinally(() -> progress.set(false))
                .execute(this::addClubs);
    }

    private void addClubs(List<ClubItem> clubItems) {
        Request.with(this, Api.class)
                .create(api -> api.sortClubs(new SortingRequest(clubItems, DataUtils.getLocation())))
                .execute(result -> clubs.addAll(result));

    }

    @Override
    public String getTitle() {
        return getActivity() == null ? "" : getActivity().getString(R.string.clubs_guest);
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
        return R.layout.layout_guest_visit;
    }

    public void requestGuestVisit(View view) {

    }
}
