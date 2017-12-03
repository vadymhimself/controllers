package ru.xfit.model.service;

import android.location.Location;

import com.controllers.Task;
import com.google.gson.Gson;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import ru.xfit.model.data.ErrorException;
import ru.xfit.model.data.ErrorResponse;
import ru.xfit.model.data.FeedbackRequest;
import ru.xfit.model.data.UserData;
import ru.xfit.model.data.auth.AuthRequest;
import ru.xfit.model.data.auth.AuthResponse;
import ru.xfit.model.data.club.AddClassResponse;
import ru.xfit.model.data.club.ClubItem;
import ru.xfit.model.data.club.LinkRequest;
import ru.xfit.model.data.club.SortingRequest;
import ru.xfit.model.data.common.EmptyBody;
import ru.xfit.model.data.contract.Contract;
import ru.xfit.model.data.contract.SuspendRequest;
import ru.xfit.model.data.news.News;
import ru.xfit.model.data.notification.NotificationSettings;
import ru.xfit.model.data.notifications.Notification;
import ru.xfit.model.data.notifications.NotificationSettingsRequest;
import ru.xfit.model.data.notifications.RegisterDeviceRequest;
import ru.xfit.model.data.notifications.ResultResponse;
import ru.xfit.model.data.phoneConfiramtion.ConfirmationRequest;
import ru.xfit.model.data.phoneConfiramtion.ConfirmationResponse;
import ru.xfit.model.data.register.RegisterRequest;
import ru.xfit.model.data.schedule.Schedule;
import ru.xfit.model.data.schedule.ScheduleList;
import ru.xfit.model.data.schedule.Trainer;
import ru.xfit.model.data.storage.preferences.PreferencesStorage;
import ru.xfit.model.retrorequest.NetworkError;
import ru.xfit.model.retrorequest.TaskBuilder;

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

            throw new NetworkError(response.code(), response.message(), errorResponse);
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
    public Task<NotificationSettings> getNotifySettings() {
        return new Task<NotificationSettings>() {
            @Override
            public NotificationSettings exec() throws Throwable {
                return storage.getSettings();
            }

            @Override
            public void cancel() {

            }
        };
    }

    @Override
    public Task<Void> saveNotifySettings(NotificationSettings settings) {
        return new Task<Void>() {
            @Override
            public Void exec() throws Throwable {
                storage.saveSettings(settings);
                return null;
            }

            @Override
            public void cancel() {

            }
        };
    }

    @Override
    public Task<List<ClubItem>> getClubs() {
        return new Task<List<ClubItem>>() {
            @Override
            public List<ClubItem> exec() throws Throwable {
                return executeSync(networkInterface.getClubs());
            }

            @Override
            public void cancel() {

            }
        };
    }

    @Override
    public Task<Schedule> getClassesForClub(String id) {
        return TaskBuilder.from(networkInterface.getClasses(id));
    }

    @Override
    public Task<ScheduleList> getMySchedule(int year, int week) {
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

    @Override
    public Task<List<Trainer>> getTrainers(String id) {
        return TaskBuilder.from(networkInterface.getTrainers(id));
    }

    @Override
    public Task<List<Contract>> getContracts() {
        return TaskBuilder.from(networkInterface.getContracts());
    }

    @Override
    public Task<Contract> suspendContract(SuspendRequest request) {
        return TaskBuilder.from(networkInterface.suspendContract(request));
    }

    @Override
    public Task<List<Contract>> linkToClub(String clubId) {
        return TaskBuilder.from(networkInterface.linkToCLub(new LinkRequest(clubId)));
    }

    @Override
    public Task<List<ClubItem>> sortClubs(SortingRequest sortingRequest) {
        return new Task<List<ClubItem>>() {
            @Override
            public List<ClubItem> exec() throws Throwable {
                if (sortingRequest != null) {
                    Comparator comp = (Comparator<ClubItem>) (o, o2) -> {
                        float[] result1 = new float[3];
                        Location.distanceBetween(sortingRequest.location.getLatitude(), sortingRequest.location.getLongitude(),
                                o.latitude, o.longitude, result1);
                        Float distance1 = result1[0];

                        float[] result2 = new float[3];
                        Location.distanceBetween(sortingRequest.location.getLatitude(), sortingRequest.location.getLongitude(),
                                o2.latitude, o2.longitude, result2);
                        Float distance2 = result2[0];

                        return distance1.compareTo(distance2);
                    };

                    Collections.sort(sortingRequest.toSort, comp);
                } else {
                    Collections.sort(sortingRequest.toSort, (clubItem1, clubItem2) -> clubItem1.city.compareTo(clubItem2.city));
                }

                return sortingRequest.toSort;
            }

            @Override
            public void cancel() {

            }
        };
    }

    @Override
    public Task<ResultResponse> registerDevice(String gcmToken) {
        return TaskBuilder.from(networkInterface.registerDevice(new RegisterDeviceRequest(gcmToken)));
    }

    @Override
    public Task<List<Notification>> getNotifications() {
        return TaskBuilder.from(networkInterface.getNotifications());
    }

    @Override
    public Task<ResultResponse> saveNotificationSettings(String isNotify) {
        return TaskBuilder.from(networkInterface.saveNotificationsSettings(new NotificationSettingsRequest(isNotify)));
    }

    @Override
    public Task<List<News>> getClubNews(String clubId) {
        return TaskBuilder.from(networkInterface.getClubNews(clubId));
    }

    @Override
    public Task<ResultResponse> sendFeedback(FeedbackRequest request) {
        return TaskBuilder.from(networkInterface.sendFeedback(request));
    }
}
