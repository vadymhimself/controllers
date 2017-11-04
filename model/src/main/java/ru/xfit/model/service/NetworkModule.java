package ru.xfit.model.service;

import com.google.gson.Gson;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.xfit.model.data.UserData;
import ru.xfit.model.data.auth.AuthResponse;
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

//        Interceptor commonFieldsInterceptor = chain -> {
//            // common params interceptor
//            Request req = chain.request();
//            if (userData.isLoggedIn()) {
//                HttpUrl url = req.url().newBuilder()
//                        .addQueryParameter("access_token", userData.getAccessToken().accessToken)
//                        .build();
//                req = req.newBuilder().url(url).build();
//            }
//            return chain.proceed(req);
//        };
//        interceptors.add(commonFieldsInterceptor);

        Interceptor refreshAuthTokenInterceptor = chain -> {
            Request request = chain.request();

            Request.Builder builder = request.newBuilder();
            builder.header("Accept", "application/json");

            HttpUrl urlReq = request.url().newBuilder()
                    .setQueryParameter("token", storage.getCurrentUser().token)
                    .build();
            request = request.newBuilder().url(urlReq).build();

//            request = builder.build(); //overwrite old request
            Response response = chain.proceed(request);

            if (response.code() == 401) { //if unauthorized

                Request.Builder authBuilder = new Request.Builder();
                builder.url(provideBaseUrl() + "auth/phone");

                Request authRequest = authBuilder.build();

                HttpUrl authUrl = authRequest.url().newBuilder()
                        .addQueryParameter("id", storage.getCurrentUser().phone)
                        .addQueryParameter("pass", storage.getCurrentUser().pass)
                        .build();

                authRequest = authRequest.newBuilder().url(authUrl).build();

                Response authResponse = chain.proceed(authRequest);

                int code = authResponse.code() / 100; //refresh token
                if (code != 2) { //if refresh token failed for some reason
                    if (code == 4) //only if response is 400, 500 might mean that token was not updated
                        storage.clearCurrentUser();
                    return response; //if token refresh failed - show error to user
                }

                String json = response.body().string();
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
