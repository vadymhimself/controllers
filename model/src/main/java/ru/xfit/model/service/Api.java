package ru.xfit.model.service;

import com.controllers.Task;
import ru.xfit.model.data.AccessToken;
import ru.xfit.model.data.User;

import java.util.List;


public interface Api {
    Task<AccessToken> getAccessToken(String url, String clientId,
                                     String clientSecret, String code);

    Task<User> getCurrentUser();

    Task<List<User>> getFollowers(String username);

    Task<List<User>> getFollowing(String username);

    Task<Object> isFollowing(String username);

    Task<User> getUser(String username);

    Task<Object> toggleFollowing(User user, boolean follow);

    Task<User> login(String code);

}
