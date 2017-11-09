package ru.xfit.model.service;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import com.google.gson.Gson;
import com.hwangjr.rxbus.Bus;
import dagger.Module;
import dagger.Provides;
import okhttp3.*;
import ru.xfit.model.data.auth.AuthRequest;
import ru.xfit.model.data.auth.AuthResponse;
import ru.xfit.model.data.storage.preferences.PreferencesStorage;
import ru.xfit.model.retrorequest.LogoutEvent;

import javax.inject.Named;
import javax.inject.Singleton;

@Module
class AuthModule {

    @Provides
    @Singleton
    @Named("tokenInterceptor")
    Interceptor provideTokenInterceptor(@Named("baseUrl") String baseUrl,
                                        PreferencesStorage storage,
                                        Bus bus,
                                        Context context) {
        return chain -> {
            Request request = chain.request();

            Request.Builder builder = request.newBuilder();
            builder.header("Accept", "application/json");

            HttpUrl urlReq = request.url().newBuilder()
                    .setQueryParameter("token", storage.getCurrentUser().token)
                    .build();
            request = request.newBuilder().url(urlReq).build();

            Response response = chain.proceed(request);

            if (response.code() == 401) { //if unauthorized

                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                AuthRequest body = new AuthRequest(storage.getCurrentUser().pass, storage.getCurrentUser().phone);
                RequestBody requestBody = RequestBody.create(JSON, new Gson().toJson(body));
                Request authRequest = new Request.Builder()
                        .url(baseUrl + "auth/phone")
                        .post(requestBody)
                        .build();
                Response authResponse = chain.proceed(authRequest);

                int code = authResponse.code() / 100; //refresh token
                if (code != 2) { //if refresh token failed for some reason
                    storage.clearCurrentUser();
                    new Handler(Looper.getMainLooper()).post(() -> {
                        bus.post(new LogoutEvent());
                    });
                    return response; //if token refresh failed - show error to user
                }

                String json = authResponse.body().string();
                AuthResponse authJson = new Gson().fromJson(json, AuthResponse.class);
                authJson.user.language = authJson.language;
                authJson.user.city = authJson.city;
                authJson.user.token = authJson.token;
                storage.saveCurrentUser(authJson.user);

                if (authJson.token != null) {

                    HttpUrl url = request.url().newBuilder()
                            .setQueryParameter("token", storage.getCurrentUser().token)
                            .build();
                    request = request.newBuilder().url(url).build();
                    return chain.proceed(request); //repeat request with new token
                }
            }

            return response;
        };
    }
}
