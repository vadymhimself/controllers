package ru.xfit.screens.contacts;

import android.view.View;

import ru.xfit.R;
import ru.xfit.misc.adapters.BaseVM;
import ru.xfit.model.data.club.ClubItem;
import ru.xfit.screens.FeedbackController;
import ru.xfit.screens.XFitController;

/**
 * Created by TESLA on 27.11.2017.
 */

public class ContactVM implements BaseVM {
    public final ClubItem clubItem;
    public XFitController controller;
    private OnClubClickListener clickListener;

    public ContactVM(ClubItem clubItem, XFitController controller, OnClubClickListener clickListener) {
        this.clubItem = clubItem;
        this.controller = controller;
        this.clickListener = clickListener;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_contact;
    }

    public void onItemClick(View view) {
        this.clickListener.onClick(clubItem);
    }
}
