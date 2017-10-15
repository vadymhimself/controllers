package ru.xfit.model.service;

import com.controllers.Task;
import ru.xfit.model.data.AccessToken;
import ru.xfit.model.data.User;
import ru.xfit.model.data.UserData;
import ru.xfit.model.retrorequest.TaskBuilder;

import java.util.List;

final class ApiImpl implements Api {

    private final GitHubNetworkInterface networkInterface;
    private final UserData userData;

    ApiImpl(GitHubNetworkInterface networkInterface, UserData userData) {
        this.networkInterface = networkInterface;
        this.userData = userData;
    }

    public Task<AccessToken> getAccessToken(String url, String clientId,
                                            String clientSecret, String code) {
        return TaskBuilder.from(networkInterface.getAccessToken(url, clientId,
                clientSecret, code));
    }

    public Task<User> getCurrentUser() {
        return TaskBuilder.from(networkInterface.getCurrentUser());
    }

    public Task<List<User>> getFollowers(String username) {
        return TaskBuilder.from(networkInterface.getFollowers(username));
    }

    public Task<List<User>> getFollowing(String username) {
        return TaskBuilder.from(networkInterface.getFollowing(username));
    }

    public Task<Object> isFollowing(String username) {
        return TaskBuilder.from(networkInterface.isFollowing(username));
    }

    public Task<User> getUser(String username) {
        return TaskBuilder.from(networkInterface.getUser(username));
    }


    public Task<Object> toggleFollowing(User user, boolean follow) {
        if (follow) {
            return TaskBuilder.from(networkInterface.follow(user.login));
        } else {
            return TaskBuilder.from(networkInterface.unFollow(user.login));
        }
    }

    public Task<User> login(String code) {
        return TaskBuilder.fromAsync(() -> {

            AccessToken token = getAccessToken(
                    AccessToken.ACCESS_TOKEN_URL,
                    AccessToken.CLIENT_ID,
                    AccessToken.CLIENT_SECRET,
                    code)
                    .exec();

            userData.setAccessToken(token);

            return getCurrentUser().exec();
        });
    }
}
