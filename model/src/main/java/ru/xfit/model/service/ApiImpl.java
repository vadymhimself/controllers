package ru.xfit.model.service;

import com.controllers.Task;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Response;
import ru.xfit.model.data.AccessToken;
import ru.xfit.model.data.ErrorException;
import ru.xfit.model.data.ErrorResponse;
import ru.xfit.model.data.User;
import ru.xfit.model.data.UserData;
import ru.xfit.model.data.auth.AuthRequest;
import ru.xfit.model.data.auth.AuthResponse;
import ru.xfit.model.data.club.AddClassResponse;
import ru.xfit.model.data.club.Class;
import ru.xfit.model.data.club.Club;
import ru.xfit.model.data.common.EmptyBody;
import ru.xfit.model.data.phoneConfiramtion.ConfirmationRequest;
import ru.xfit.model.data.phoneConfiramtion.ConfirmationResponse;
import ru.xfit.model.data.register.RegisterRequest;
import ru.xfit.model.data.schedule.ScheduleClub;
import ru.xfit.model.data.schedule.ScheduleList;
import ru.xfit.model.data.storage.Storage;
import ru.xfit.model.data.storage.preferences.PreferencesStorage;
import ru.xfit.model.retrorequest.TaskBuilder;

import java.util.List;

final class ApiImpl implements Api {

    private final NetworkInterface networkInterface;
    private final PreferencesStorage storage;
    private final UserData userData;

    ApiImpl(NetworkInterface networkInterface, UserData userData, PreferencesStorage preferencesStorage) {
        this.networkInterface = networkInterface;
        this.userData = userData;
        this.storage = preferencesStorage;
    }

    private <T> T executeSync(Call<T> call) throws Throwable {
        Response<T> response = call.execute();
        if (response.isSuccessful()) {
            return response.body();
        } else {
            String json = response.errorBody().string();
            ErrorResponse errorResponse = new Gson().fromJson(json, ErrorResponse.class);

            throw new ErrorException(errorResponse.message);
        }
    }

    @Override
    public Task<AuthResponse> authByPhone(String phone, String password) {
        //String type, AuthRequest request
        AuthRequest request = new AuthRequest();
        request.id = phone;
        request.pass = password;
        return new Task<AuthResponse>() {
            @Override
            public AuthResponse exec() throws Throwable {
                return executeSync(networkInterface.auth("phone", request));
            }

            @Override
            public void cancel() {

            }
        };
    }

    @Override
    public Task<AuthResponse> register(RegisterRequest request) {
        return new Task<AuthResponse>() {
            @Override
            public AuthResponse exec() throws Throwable {
                return executeSync(networkInterface.register(request));
            }

            @Override
            public void cancel() {

            }
        };
    }

    @Override
    public Task<ConfirmationResponse> pleaseConfirm(String phone) {
        ConfirmationRequest request = new ConfirmationRequest(phone);
        return new Task<ConfirmationResponse>() {
            @Override
            public ConfirmationResponse exec() throws Throwable {
                return executeSync(networkInterface.pleaseConfirm(request));
            }

            @Override
            public void cancel() {

            }
        };
    }

    @Override
    public Task<ru.xfit.model.data.auth.User> getSavedUser() {
        return new Task<ru.xfit.model.data.auth.User>() {
            @Override
            public ru.xfit.model.data.auth.User exec() throws Throwable {
                return storage.getCurrentUser();
            }

            @Override
            public void cancel() {

            }
        };
    }

    @Override
    public Task<Void> deleteSavedUser() {
        return new Task<Void>() {
            @Override
            public Void exec() throws Throwable {
                storage.clearCurrentUser();
                return null;
            }

            @Override
            public void cancel() {

            }
        };
    }

    @Override
    public Task<Void> saveUser(ru.xfit.model.data.auth.User user) {
        return new Task<Void>() {
            @Override
            public Void exec() throws Throwable {
                storage.saveCurrentUser(user);
                return null;
            }

            @Override
            public void cancel() {

            }
        };
    }

    @Override
    public Task<List<Club>> getClubs() {
        return null;
    }

    @Override
    public Task<ScheduleClub> getClasses(String id) {
        return TaskBuilder.from(networkInterface.getClasses(id));
    }

    @Override
    public Task<ScheduleList> getMySchedule(String year, String week) {
        return TaskBuilder.from(networkInterface.getSchedule(year, week));
    }

    @Override
    public Task<ScheduleList> getMySchedule() {
        return TaskBuilder.from(networkInterface.getSchedule());
    }

    @Override
    public Task<AddClassResponse> addClass(String id) {
        return TaskBuilder.from(networkInterface.addClass(id, new EmptyBody()));
    }

    @Override
    public Task<Void> deleteClass(String id) {
        return TaskBuilder.from(networkInterface.deleteClass(id));
    }


//    public Task<AccessToken> getAccessToken(String url, String clientId,
//                                            String clientSecret, String code) {
//        return TaskBuilder.from(networkInterface.getAccessToken(url, clientId,
//                clientSecret, code));
//    }
//
//    public Task<User> getCurrentUser() {
//        return TaskBuilder.from(networkInterface.getCurrentUser());
//    }
//
//    public Task<List<User>> getFollowers(String username) {
//        return TaskBuilder.from(networkInterface.getFollowers(username));
//    }
//
//    public Task<List<User>> getFollowing(String username) {
//        return TaskBuilder.from(networkInterface.getFollowing(username));
//    }
//
//    public Task<Object> isFollowing(String username) {
//        return TaskBuilder.from(networkInterface.isFollowing(username));
//    }
//
//    public Task<User> getUser(String username) {
//        return TaskBuilder.from(networkInterface.getUser(username));
//    }
//
//
//    public Task<Object> toggleFollowing(User user, boolean follow) {
//        if (follow) {
//            return TaskBuilder.from(networkInterface.follow(user.login));
//        } else {
//            return TaskBuilder.from(networkInterface.unFollow(user.login));
//        }
//    }
//
//    public Task<User> login(String code) {
//        return TaskBuilder.fromAsync(() -> {
//
//            AccessToken token = getAccessToken(
//                    AccessToken.ACCESS_TOKEN_URL,
//                    AccessToken.CLIENT_ID,
//                    AccessToken.CLIENT_SECRET,
//                    code)
//                    .exec();
//
//            userData.setAccessToken(token);
//
//            return getCurrentUser().exec();
//        });
//    }
}
