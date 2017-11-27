package ru.xfit.screens.contacts;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.view.View;

import com.hwangjr.rxbus.annotation.Subscribe;

import ru.xfit.R;
import ru.xfit.databinding.LayoutCallMeBinding;
import ru.xfit.domain.App;
import ru.xfit.misc.NavigationClickListener;
import ru.xfit.misc.events.OnBackEvent;
import ru.xfit.misc.events.OptionsItemSelectedEvent;
import ru.xfit.model.data.club.ClubItem;
import ru.xfit.model.data.storage.preferences.PreferencesManager;
import ru.xfit.model.data.storage.preferences.PreferencesStorage;
import ru.xfit.screens.FeedbackController;

/**
 * Created by TESLA on 27.11.2017.
 */

public class CallMeController extends FeedbackController<LayoutCallMeBinding> implements NavigationClickListener {

    public ObservableField<String> phone = new android.databinding.ObservableField<>("");
    public ObservableField<String> name = new android.databinding.ObservableField<>("");

    CallMeController(ObservableArrayList<ClubItem> clubs) {
        this.clubs = clubs;

        PreferencesStorage storage = new PreferencesStorage(App.getContext());
        name.set(storage.getCurrentUser().name);
        phone.set(storage.getCurrentUser().phone);
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_call_me;
    }

    @Override
    public String getTitle() {
        return App.getContext().getResources().getString(R.string.call_me_title);
    }

    @Override
    public void onNavigationClick() {
        if (prevPopup.get() != null) {
            prevPopup.get().dismiss();
        }
        back();
    }

    @Subscribe
    public void onTriggerBackPressed(OptionsItemSelectedEvent onBackEvent) {
        if (prevPopup.get() != null) {
            prevPopup.get().dismiss();
        }
    }

    public void callMe(View view) {

    }
}
