package ru.xfit.model.service;

import com.controllers.Task;

import java.util.Date;
import java.util.List;

import ru.xfit.model.data.FeedbackRequest;
import ru.xfit.model.data.auth.AuthResponse;
import ru.xfit.model.data.club.AddClassResponse;
import ru.xfit.model.data.club.ClubItem;
import ru.xfit.model.data.club.SortingRequest;
import ru.xfit.model.data.contract.Contract;
import ru.xfit.model.data.contract.SuspendRequest;
import ru.xfit.model.data.roomdata.model.DiaryItem;
import ru.xfit.model.data.news.News;
import ru.xfit.model.data.notification.NotificationSettings;
import ru.xfit.model.data.notifications.Notification;
import ru.xfit.model.data.notifications.ResultResponse;
import ru.xfit.model.data.phoneConfiramtion.ConfirmationResponse;
import ru.xfit.model.data.register.RegisterRequest;
import ru.xfit.model.data.schedule.Schedule;
import ru.xfit.model.data.schedule.ScheduleList;
import ru.xfit.model.data.schedule.Trainer;


public interface Api {
//    Task<AccessToken> getAccessToken(String url, String clientId,
//                                     String clientSecret, String code);
//
//    Task<User> getCurrentUser();
//
//    Task<List<User>> getFollowers(String username);
//
//    Task<List<User>> getFollowing(String username);
//
//    Task<Object> isFollowing(String username);
//
//    Task<User> getUser(String username);
//
//    Task<Object> toggleFollowing(User user, boolean follow);
//
//    Task<User> login(String code);

    Task<AuthResponse> authByPhone(String phone, String password);

    Task<AuthResponse> register(RegisterRequest request);

    Task<ConfirmationResponse> pleaseConfirm(String phone);

    Task<ru.xfit.model.data.auth.User> getSavedUser();

    Task<Void> deleteSavedUser();

    Task<Void> saveUser(ru.xfit.model.data.auth.User user);

    Task<NotificationSettings> getNotifySettings();

    Task<Void> saveNotifySettings(NotificationSettings settings);

    Task<List<ClubItem>> getClubs();

    Task<Schedule> getClassesForClub(String id);

    Task<ScheduleList> getMySchedule(int year, int week);

    Task<ScheduleList> getMySchedule();

    Task<AddClassResponse> addClass(String id);

    Task<Void> deleteClass(String id);

    Task<List<Trainer>> getTrainers(String id);

    Task<List<Contract>> getContracts();

    Task<Contract> suspendContract(SuspendRequest request);

    Task<List<Contract>> linkToClub(String clubId);

    Task<List<ClubItem>> sortClubs(SortingRequest sortingRequest);

    Task<ResultResponse> registerDevice(String gcmToken);

    Task<List<Notification>> getNotifications();

    Task<ResultResponse> saveNotificationSettings(String isNotify);

    Task<List<News>> getClubNews(String clubId);

    Task<ResultResponse> sendFeedback(FeedbackRequest request);

    Task<List<DiaryItem>> getDiaryItems(String month);

    Task<Integer> setDiaryItem(String month, String day, long mass);

    Task<Void> insertDiaryItems(List<DiaryItem> diaryItems);
}
