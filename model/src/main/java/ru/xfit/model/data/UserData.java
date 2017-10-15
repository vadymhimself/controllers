package ru.xfit.model.data;


import javax.inject.Inject;

/**
 * Created by Vadym Ovcharenko
 * 06.11.2016.
 */

public final class UserData {

    private static final String APP_PREF = "sujdi1375t-yh";
    private static final String KEY_CURRENT_USER_ID = "_curr_user_id";

    private AccessToken accessToken;

    @Inject UserData() {
        accessToken = AccessToken.restore();
    }

    public void setAccessToken(AccessToken accessToken) {
        this.accessToken = accessToken;
        accessToken.save();
    }

    public AccessToken getAccessToken() {
        return accessToken;
    }

    public boolean isLoggedIn() {
        return accessToken != null;
    }
}
