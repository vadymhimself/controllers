package ru.xfit.model.data;

import android.content.SharedPreferences;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Vadym Ovcharenko
 * 06.11.2016.
 */

public class AccessToken {
    public static final String ACCESS_TOKEN_URL = "https://github" +
            ".com/login/oauth/access_token";
    public static final String CLIENT_ID = "29d1e2e1ac8187d42a66";
    public static final String CLIENT_SECRET =
            "3ee945256d99110f00a771373584e1422435f292";

    @SerializedName("access_token")
    public String accessToken;
    @SerializedName("token_type")
    public String tokenType;

    public AccessToken(String accessToken, String tokenType) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
    }

    public static AccessToken restore() {
        // TODO
        return null;
    }

    public void save() {
        // TODO:
    }
}
