package ru.xfit.model.service;

import com.controllers.Task;
import ru.xfit.model.data.AccessToken;
import ru.xfit.model.data.User;
import ru.xfit.model.data.auth.AuthRequest;
import ru.xfit.model.data.auth.AuthResponse;
import ru.xfit.model.data.club.AddClassResponse;
import ru.xfit.model.data.club.Class;
import ru.xfit.model.data.club.Club;
import ru.xfit.model.data.phoneConfiramtion.ConfirmationRequest;
import ru.xfit.model.data.phoneConfiramtion.ConfirmationResponse;
import ru.xfit.model.data.register.RegisterRequest;
import ru.xfit.model.data.schedule.ScheduleClub;
import ru.xfit.model.data.schedule.ScheduleList;

import java.util.List;


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

    Task<List<Club>> getClubs();

    Task<ScheduleClub> getClasses(String id);

    Task<ScheduleList> getMySchedule(String year, String week);

    Task<AddClassResponse> addClass(String id);

    Task<Void> deleteClass(String id);

}
