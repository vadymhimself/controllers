package ru.xfit.model.data;

import android.content.SharedPreferences;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Artem on 01.11.2016.
 */

public class User implements Serializable {

    public int id;
    public String login;
    @SerializedName("avatar_url") public String avatarUrl;

    private User(int id, String login, String avatarUrl) {
        this.login = login;
        this.avatarUrl = avatarUrl;
    }
}
