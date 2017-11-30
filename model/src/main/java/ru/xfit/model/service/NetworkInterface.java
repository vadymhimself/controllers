package ru.xfit.model.service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.xfit.model.data.auth.AuthRequest;
import ru.xfit.model.data.auth.AuthResponse;
import ru.xfit.model.data.club.AddClassResponse;
import ru.xfit.model.data.club.ClubItem;
import ru.xfit.model.data.club.LinkRequest;
import ru.xfit.model.data.common.EmptyBody;
import ru.xfit.model.data.contract.Contract;
import ru.xfit.model.data.contract.SuspendRequest;
import ru.xfit.model.data.news.News;
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

/**
 * Created by TESLA on 25.10.2017.
 */

public interface NetworkInterface {
    ///auth/{type}
    @POST("auth/{type}")
    Call<AuthResponse> auth(@Path("type") String type, @Body AuthRequest request);
    ///register
    @POST("register")
    Call<AuthResponse> register(@Body RegisterRequest request);
    ///phone/sendConfirmation
    @POST("phone/sendConfirmation")
    Call<ConfirmationResponse> pleaseConfirm(@Body ConfirmationRequest request);

    ///clubs
    @GET("clubs")
    Call<List<ClubItem>> getClubs();

    ///clubs/{id}/classes
    @GET("clubs/{id}/classes")
    Call<Schedule> getClasses(@Path("id") String id);

    @GET("clubs/{id}/trainers")
    Call<List<Trainer>> getTrainers(@Path("id") String id);

    ///me/classes/{id}
    @GET("me/classes")
    Call<ScheduleList> getSchedule(@Query("year") int year,
                                   @Query("week") int week);

    @GET("me/classes")
    Call<ScheduleList> getSchedule();

    @POST("me/classes/{id}")
    Call<AddClassResponse> addClass(@Path("id") String id, @Body EmptyBody request);

    ///me/classes/{id}
    @DELETE("me/classes/{id}")
    Call<Void> deleteClass(@Path("id") String id);

    @GET("me/contracts")
    Call<List<Contract>> getContracts();

    @POST("me/contracts/suspend")
    Call<Contract> suspendContract(@Body SuspendRequest request);

    @PUT("me/clubs")
    Call<List<Contract>> linkToCLub(@Body LinkRequest request );

    @POST("me/notifications/registerDevice")
    Call<ResultResponse> registerDevice(@Body RegisterDeviceRequest request);

    @GET("me/notifications")
    Call<List<Notification>> getNotifications();

    @PUT("me/notifications/settings")
    Call<ResultResponse> saveNotificationsSettings(@Body NotificationSettingsRequest request);

    @GET("clubs/{clubId}/news")
    Call<List<News>> getClubNews(@Path("clubId") String clubId);
}
