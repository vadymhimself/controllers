package ru.xfit.screens.contacts;

import android.databinding.ObservableArrayList;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ru.xfit.R;
import ru.xfit.databinding.PopupRecyclerBinding;
import ru.xfit.misc.adapters.FilterableAdapter;
import ru.xfit.model.data.club.ClubItem;
import ru.xfit.screens.BlankToolbarController;
import ru.xfit.screens.XFitController;
import ru.xfit.screens.clubs.ClubsFilter;

/**
 * Created by TESLA on 27.11.2017.
 */

public class PopupController extends XFitController<PopupRecyclerBinding> {

    public FilterableAdapter adapter = new FilterableAdapter<>(new ArrayList<>());
    public final ClubsFilter clubsFilter = new ClubsFilter("");

    public PopupController(ObservableArrayList<ClubItem> clubs, OnClubClickListener clickListener) {
        adapter.addFilter(clubsFilter);

        List<ContactVM> toAdd = new ArrayList<>();
        for (int i = 0; i < clubs.size(); i++) {
            toAdd.add(new ContactVM(clubs.get(i), this, clickListener));
        }
        adapter.addAll(toAdd);


    }

    @Override
    public int getLayoutId() {
        return R.layout.popup_recycler;
    }
}
