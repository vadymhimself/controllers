package ru.xfit.model.service;

import android.util.Log;

import com.google.gson.Gson;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.xfit.model.data.UserData;
import ru.xfit.model.data.auth.AuthRequest;
import ru.xfit.model.data.auth.AuthResponse;
import ru.xfit.model.data.auth.Language;
import ru.xfit.model.data.auth.User;
import ru.xfit.model.data.common.City;
import ru.xfit.model.data.storage.preferences.PreferencesStorage;
import ru.xfit.model.service.GitHubNetworkModule;

import javax.inject.Named;
import javax.inject.Singleton;

import java.util.ArrayList;
import java.util.List;

@Module
class NetworkModule {

    @Provides
    @Named("baseUrl")
    String provideBaseUrl() {
        return "https://xfit-api.herokuapp.com/v1/";
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(List<Interceptor> interceptors) {
        OkHttpClient.Builder builder =  new OkHttpClient.Builder();
        for (Interceptor interceptor : interceptors) {
            builder.addInterceptor(interceptor);
        }
        return builder.build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(@Named("baseUrl") String baseUrl, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    List<Interceptor> provideInterceptors(PreferencesStorage storage) {
        List<Interceptor> interceptors = new ArrayList<>();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        interceptors.add(loggingInterceptor);

        Interceptor refreshAuthTokenInterceptor = chain -> {
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
                        .url(provideBaseUrl() + "auth/phone")
                        .post(requestBody)
                        .build();
                Response authResponse = chain.proceed(authRequest);

                int code = authResponse.code() / 100; //refresh token
                if (code != 2) { //if refresh token failed for some reason
                    if (code == 4) //only if response is 400, 500 might mean that token was not updated
                        storage.clearCurrentUser();
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

        interceptors.add(refreshAuthTokenInterceptor);

        return interceptors;
    }


    @Provides
    @Singleton
    NetworkInterface provideXfitApi(Retrofit retrofit) {
        return retrofit.create(NetworkInterface.class);
    }
}
