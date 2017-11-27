package ru.xfit.screens.contacts;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.view.View;

import ru.xfit.R;
import ru.xfit.databinding.LayoutCallMeBinding;
import ru.xfit.domain.App;
import ru.xfit.misc.NavigationClickListener;
import ru.xfit.model.data.club.ClubItem;
import ru.xfit.model.data.storage.preferences.PreferencesStorage;
import ru.xfit.screens.FeedbackController;

/**
 * Created by TESLA on 28.11.2017.
 */

public class SendFeedbackController  extends FeedbackController<LayoutCallMeBinding> implements NavigationClickListener {

    public ObservableField<String> feedback = new ObservableField<>();

    SendFeedbackController(ObservableArrayList<ClubItem> clubs) {
        this.clubs = clubs;

//        PreferencesStorage storage = new PreferencesStorage(App.getContext());
//        name.set(storage.getCurrentUser().name);
//        phone.set(storage.getCurrentUser().phone);
    }

    @Override
    public void onNavigationClick() {
        if (prevPopup.get() != null) {
            prevPopup.get().dismiss();
        }
        back();
    }

    @Override
    public String getTitle() {
        return App.getContext().getResources().getString(R.string.send_feedback_title);
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_send_feedback;
    }

    public void sendMe(View view) {

    }
}
