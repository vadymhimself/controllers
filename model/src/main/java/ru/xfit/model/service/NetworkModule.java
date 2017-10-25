package ru.xfit.model.service;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.xfit.model.data.UserData;
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
        return "http://xfit-api.azurewebsites.net/v1/";
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
    List<Interceptor> provideInterceptors(UserData userData) {
        List<Interceptor> interceptors = new ArrayList<>();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        interceptors.add(loggingInterceptor);

        Interceptor commonFieldsInterceptor = chain -> {
            // common params interceptor
            Request req = chain.request();
            if (userData.isLoggedIn()) {
                HttpUrl url = req.url().newBuilder()
                        .addQueryParameter("access_token", userData.getAccessToken().accessToken)
                        .build();
                req = req.newBuilder().url(url).build();
            }
            return chain.proceed(req);
        };

        interceptors.add(commonFieldsInterceptor);
        return interceptors;
    }


    @Provides
    @Singleton
    NetworkInterface provideXfitApi(Retrofit retrofit) {
        return retrofit.create(NetworkInterface.class);
    }
}
