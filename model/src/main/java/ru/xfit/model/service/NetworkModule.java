package ru.xfit.model.service;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Module(includes = {AuthModule.class})
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
    List<Interceptor> provideInterceptors(@Named("tokenInterceptor") Interceptor tokenInterceptor) {
        List<Interceptor> interceptors = new ArrayList<>();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        interceptors.add(loggingInterceptor);
        interceptors.add(tokenInterceptor);

        return interceptors;
    }


    @Provides
    @Singleton
    NetworkInterface provideXfitApi(Retrofit retrofit) {
        return retrofit.create(NetworkInterface.class);
    }
}
