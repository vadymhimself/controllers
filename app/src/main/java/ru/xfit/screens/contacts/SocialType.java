package ru.xfit.screens.contacts;

import ru.xfit.R;
import ru.xfit.domain.App;

/**
 * Created by TESLA on 27.11.2017.
 */

public enum SocialType {
    TT_LINK(App.getContext().getResources().getString(R.string.contacts_tt_link)),
    FB_LINK(App.getContext().getResources().getString(R.string.contacts_fb_link)),
    IN_LINK(App.getContext().getResources().getString(R.string.contacts_in_link)),
    FS_LINK(App.getContext().getResources().getString(R.string.contacts_fs_link)),
    VK_LINK(App.getContext().getResources().getString(R.string.contacts_vk_link)),
    YT_LINK(App.getContext().getResources().getString(R.string.contacts_yt_link)),
    OK_LINK(App.getContext().getResources().getString(R.string.contacts_ok_link)),
    GP_LINK(App.getContext().getResources().getString(R.string.contacts_gp_link));

    private String socialLink;
    SocialType(String socialLink) {
        this.socialLink = socialLink;
    }

    public String getSocialLink() {
        return socialLink;
    }
}
