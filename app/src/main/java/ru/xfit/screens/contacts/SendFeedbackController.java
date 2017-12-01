package ru.xfit.screens.contacts;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.view.View;

import com.controllers.Request;

import ru.xfit.BuildConfig;
import ru.xfit.R;
import ru.xfit.databinding.LayoutSendFeedbackBinding;
import ru.xfit.domain.App;
import ru.xfit.misc.NavigationClickListener;
import ru.xfit.misc.utils.DataUtils;
import ru.xfit.misc.views.MessageDialog;
import ru.xfit.model.data.FeedbackRequest;
import ru.xfit.model.data.club.ClubItem;
import ru.xfit.model.data.storage.preferences.PreferencesStorage;
import ru.xfit.model.service.Api;
import ru.xfit.screens.FeedbackController;

/**
 * Created by TESLA on 28.11.2017.
 */

public class SendFeedbackController extends FeedbackController<LayoutSendFeedbackBinding>
        implements NavigationClickListener, MessageDialog.DialogResultListener {

    public final ObservableBoolean progress = new ObservableBoolean();
    private final String phone;
    public ObservableField<String> feedback = new ObservableField<>();

    SendFeedbackController(ObservableArrayList<ClubItem> clubs) {
        this.clubs = clubs;

        PreferencesStorage storage = new PreferencesStorage(App.getContext());
        phone = storage.getCurrentUser().phone;
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
        FeedbackRequest request = new FeedbackRequest();
        request.phone = phone;
        request.build = BuildConfig.VERSION_NAME;
        request.feedbackMessage = feedback.get();
        request.device = DataUtils.getDeviceName();
        progress.set(true);
        Request.with(this, Api.class)
                .create(api -> api.sendFeedback(request))
                .onFinally(() -> progress.set(false))
                .execute(resultResponse -> {
                    MessageDialog messageDialog = new MessageDialog.Builder()
                            .setMessage(view.getContext().getResources().getString(R.string.call_me_success))
                            .build();
                    messageDialog.setController(this);
                    messageDialog.show(getActivity().getSupportFragmentManager(), "MY_TAG");
                });
    }

    @Override
    public void onPositive(@NonNull String tag) {

    }

    @Override
    public void onNegative(@NonNull String tag) {

    }
}
