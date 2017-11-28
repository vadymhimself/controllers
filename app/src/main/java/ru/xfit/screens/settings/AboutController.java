package ru.xfit.screens.settings;

import android.content.Intent;
import android.net.Uri;
import android.widget.TextView;

import ru.xfit.R;
import ru.xfit.databinding.LayoutAboutBinding;
import ru.xfit.domain.App;
import ru.xfit.screens.XFitController;

/**
 * Created by TESLA on 28.11.2017.
 */

public class AboutController extends XFitController<LayoutAboutBinding> {

    public AboutController() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_about;
    }

    public String getTitle() {
        return App.getContext().getResources().getString(R.string.about_title);
    }

    public void getFeedback(ContactsEnum contactsEnum) {
        switch (contactsEnum){
            case WEB:
                Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse(contactsEnum.getContact()));
                getActivity().startActivity(intent);
                break;
            case EMAIL:
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse(contactsEnum.getContact()));
                getActivity().startActivity(Intent.createChooser(emailIntent, App.getContext().getResources().getString(R.string.about_email_title)));
                break;
            case PHONE:
                Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", contactsEnum.getContact(), null));
                getActivity().startActivity(phoneIntent);
                break;
            default:
                break;
        }
    }
}
