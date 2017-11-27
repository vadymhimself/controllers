package ru.xfit.screens.contacts;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.view.View;

import ru.xfit.R;
import ru.xfit.databinding.LayoutContactsBinding;
import ru.xfit.domain.App;
import ru.xfit.screens.DrawerController;

/**
 * Created by TESLA on 27.11.2017.
 */

public class ContactsController extends DrawerController<LayoutContactsBinding> {

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
        show(new CallMeController());
    }

    public void getFeedback(View view) {
        Snackbar.make(view, "Coming soon...", BaseTransientBottomBar.LENGTH_SHORT).show();
    }

    public void getFaq(View view) {
        Snackbar.make(view, "Coming soon...", BaseTransientBottomBar.LENGTH_SHORT).show();
    }
}
