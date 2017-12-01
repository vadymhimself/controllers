package ru.xfit.screens.clubs.guest_visit;

import android.view.MenuItem;

import ru.xfit.R;
import ru.xfit.databinding.LayoutGuestVisitBinding;
import ru.xfit.screens.BlankToolbarController;

/**
 * Created by aleks on 01.12.2017.
 */

public class GuestVisitController extends BlankToolbarController<LayoutGuestVisitBinding> {

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
}
