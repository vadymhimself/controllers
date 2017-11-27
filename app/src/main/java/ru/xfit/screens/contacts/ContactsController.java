package ru.xfit.screens.contacts;

import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.net.Uri;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.controllers.Request;

import java.util.List;

import ru.xfit.R;
import ru.xfit.databinding.LayoutContactsBinding;
import ru.xfit.domain.App;
import ru.xfit.misc.utils.DataUtils;
import ru.xfit.model.data.club.ClubItem;
import ru.xfit.model.data.club.SortingRequest;
import ru.xfit.model.service.Api;
import ru.xfit.screens.DrawerController;

/**
 * Created by TESLA on 27.11.2017.
 */

public class ContactsController extends DrawerController<LayoutContactsBinding> {

    public final ObservableBoolean progress = new ObservableBoolean();

    private final ObservableArrayList<ClubItem> clubs = new ObservableArrayList<>();

    public ContactsController() {
        progress.set(true);
        Request.with(this, Api.class)
                .create(Api::getClubs)
                .onFinally(() -> progress.set(false))
                .execute(this::addClubs);
    }

    private void addClubs(List<ClubItem> clubItems) {
        Request.with(this, Api.class)
                .create(api -> api.sortClubs(new SortingRequest(clubItems, DataUtils.getLocation())))
                .execute(result -> {
                    for (int i = 0; i < result.size(); i++)
                        clubs.add(result.get(i));
                });
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_contacts;
    }

    public void getSocialLink(SocialType socialType) {
        String url = socialType.getSocialLink();

        if (getActivity() == null)
            return;

        Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        getActivity().startActivity(intent);
    }

    @Override
    public String getTitle() {
        return App.getContext().getResources().getString(R.string.contacts_title);
    }

    public void getCall(View view) {
        show(new CallMeController(clubs));
    }

    public void getFeedback(View view) {
        Snackbar.make(view, "Coming soon...", BaseTransientBottomBar.LENGTH_SHORT).show();
    }

    public void getFaq(View view) {
        Snackbar.make(view, "Coming soon...", BaseTransientBottomBar.LENGTH_SHORT).show();
    }
}
