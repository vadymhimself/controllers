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
import ru.xfit.model.data.club.Class;
import ru.xfit.model.data.club.Club;
import ru.xfit.model.data.common.EmptyBody;
import ru.xfit.model.data.phoneConfiramtion.ConfirmationRequest;
import ru.xfit.model.data.phoneConfiramtion.ConfirmationResponse;
import ru.xfit.model.data.register.RegisterRequest;
import ru.xfit.model.data.schedule.ScheduleClub;
import ru.xfit.model.data.schedule.ScheduleList;

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
    Call<List<Club>> getClubs();

    ///clubs/{id}
    ///clubs/{id}/classes
    @GET("clubs/{id}/classes")
    Call<ScheduleClub> getClasses(@Path("id") String id);

    ///me/classes/{id}
    @GET("me/classes")
    Call<ScheduleList> getSchedule(@Query("year") String year,
                                   @Query("week") String week);

    @GET("me/classes")
    Call<ScheduleList> getSchedule();

    @POST("me/classes/{id}")
    Call<AddClassResponse> addClass(@Path("id") String id, @Body EmptyBody request);

    ///me/classes/{id}
    @DELETE("me/classes/{id}")
    Call<Void> deleteClass(@Path("id") String id);
}
