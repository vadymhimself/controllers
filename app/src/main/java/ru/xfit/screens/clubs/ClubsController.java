package ru.xfit.screens.clubs;

import android.databinding.Bindable;
import android.databinding.ObservableBoolean;

import com.controllers.Request;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ru.xfit.BR;
import ru.xfit.R;
import ru.xfit.databinding.LayoutClubsBinding;
import ru.xfit.misc.adapters.BaseAdapter;
import ru.xfit.misc.adapters.BaseVM;
import ru.xfit.misc.adapters.FilterableAdapter;
import ru.xfit.model.data.club.Club;
import ru.xfit.model.data.club.ClubItem;
import ru.xfit.model.service.Api;
import ru.xfit.screens.DrawerController;
import ru.xfit.screens.XFitController;

/**
 * Created by TESLA on 06.11.2017.
 *
 */

public class ClubsController extends DrawerController<LayoutClubsBinding> {

    @Bindable
    public final BaseAdapter<BaseVM> adapter = new BaseAdapter<>(new ArrayList<>());

    public final ObservableBoolean progress = new ObservableBoolean();

    public ClubsController() {
        progress.set(true);
        Request.with(this, Api.class)
                .create(Api::getClubs)
                .onFinally(() -> progress.set(false))
                .execute(this::addClubs);
    }

    @Bindable
    public boolean isClubsEmpty() {
        return adapter.getItemCount() == 0;
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
            toAdd.add(new ClubVM(club, this));
        }
        adapter.addAll(toAdd);
        notifyPropertyChanged(BR.clubsEmpty);
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_clubs;
    }
}
