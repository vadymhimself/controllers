package ru.xfit.model.service;

import ru.xfit.model.data.AccessToken;
import ru.xfit.model.data.User;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

/**
 * Created by Vadym Ovcharenko
 * 21.10.2016.
 */

interface GitHubNetworkInterface {


    @GET("/users/{name}")
    Call<User> getUser(@Path("name") String name);

    @GET("/user")
    Call<User> getCurrentUser();

    @GET("/users/{username}/followers")
    Call<List<User>> getFollowers(@Path("username") String username);

    @GET("/users/{username}/following")
    Call<List<User>> getFollowing(@Path("username") String username);

    @Headers("Accept: application/json")
    @POST
    Call<AccessToken> getAccessToken(@Url String url,
                                     @Query("client_id") String clientId,
                                     @Query("client_secret") String clientSecret,
                                     @Query("code") String code);

    @Headers("Content-Length: 0")
    @PUT("/user/following/{username}")
    Call<Object> follow(@Path("username") String username);

    @Headers("Content-Length: 0")
    @DELETE("/user/following/{username}")
    Call<Object> unFollow(@Path("username") String username);

    @Headers("Content-Length: 0")
    @GET("/user/following/{username}")
    Call<Object> isFollowing(@Path("username") String username);
}
