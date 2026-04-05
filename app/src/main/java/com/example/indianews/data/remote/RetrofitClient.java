package com.example.indianews.data.remote;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.concurrent.TimeUnit;

public class RetrofitClient {
    
    private static final String BASE_URL = "https://newsdata.io/api/1/";
    private static Retrofit retrofit = null;
    private static NewsDataApiService apiService = null;
    
    public static NewsDataApiService getApiService() {
        if (apiService == null) {
            retrofit = createRetrofit();
            apiService = retrofit.create(NewsDataApiService.class);
        }
        return apiService;
    }
    
    private static Retrofit createRetrofit() {
        // Logging interceptor for debugging
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        
        // OkHttp client with timeouts
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
        
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
