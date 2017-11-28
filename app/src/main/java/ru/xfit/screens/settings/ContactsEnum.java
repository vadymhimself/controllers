package ru.xfit.screens.settings;

import ru.xfit.R;
import ru.xfit.domain.App;

/**
 * Created by TESLA on 28.11.2017.
 */

public enum ContactsEnum {
    WEB(App.getContext().getResources().getString(R.string.about_web_link)),
    EMAIL(App.getContext().getResources().getString(R.string.about_email_link)),
    PHONE(App.getContext().getResources().getString(R.string.about_phone_link));

    private String contact;
    ContactsEnum(String contact) {
        this.contact = contact;
    }

    public String getContact() {
        return contact;
    }
}
