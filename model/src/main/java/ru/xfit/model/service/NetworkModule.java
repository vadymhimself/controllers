package ru.xfit.model.service;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.xfit.model.service.GitHubNetworkModule;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.List;

@Module(includes = {GitHubNetworkModule.class})
class NetworkModule {

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
}
