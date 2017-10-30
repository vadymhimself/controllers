package ru.xfit.model.service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ru.xfit.model.data.auth.AuthRequest;
import ru.xfit.model.data.auth.AuthResponse;
import ru.xfit.model.data.club.Class;
import ru.xfit.model.data.club.Club;
import ru.xfit.model.data.phoneConfiramtion.ConfirmationRequest;
import ru.xfit.model.data.phoneConfiramtion.ConfirmationResponse;
import ru.xfit.model.data.register.RegisterRequest;

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
    Call<List<Class>> getClasses(@Path("id") String id);

    ///me/classes/{id}
    @POST("me/classes/{id}")
    Call<Void> addClass(@Path("id") String id);

    ///me/classes/{id}
    @DELETE("me/classes/{id}")
    Call<Void> deleteClass(@Path("id") String id);
}