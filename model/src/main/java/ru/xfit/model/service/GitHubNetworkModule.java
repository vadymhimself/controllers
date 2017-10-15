package ru.xfit.model.service;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import ru.xfit.model.data.UserData;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Module
class GitHubNetworkModule {

    @Provides
    @Named("baseUrl")
    String provideBaseUrl() {
        return "https://api.github.com";
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
    GitHubNetworkInterface provideGitHubApi(Retrofit retrofit) {
        return retrofit.create(GitHubNetworkInterface.class);
    }
}
