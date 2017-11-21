package ru.xfit.model.service;

import com.controllers.Task;
import ru.xfit.model.data.auth.AuthResponse;
import ru.xfit.model.data.club.AddClassResponse;
import ru.xfit.model.data.club.Club;
import ru.xfit.model.data.club.ClubItem;
import ru.xfit.model.data.contract.Contract;
import ru.xfit.model.data.contract.SuspendRequest;
import ru.xfit.model.data.phoneConfiramtion.ConfirmationResponse;
import ru.xfit.model.data.register.RegisterRequest;
import ru.xfit.model.data.schedule.Schedule;
import ru.xfit.model.data.schedule.ScheduleList;
import ru.xfit.model.data.schedule.Trainer;

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

}
