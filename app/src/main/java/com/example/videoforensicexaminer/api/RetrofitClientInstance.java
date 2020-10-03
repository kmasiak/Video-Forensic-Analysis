package com.example.videoforensicexaminer.api;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.example.videoforensicexaminer.utils.Constants;
import com.example.videoforensicexaminer.utils.Preferences;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {

    private static Retrofit retrofit;
    private static final String BASE_URL = Constants.API_BASE_URL+"/api/";

    public static Retrofit getRetrofitInstance(Context mContext) {

        final Preferences preferences = new Preferences(mContext);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(new Interceptor() {
                    private static final String TAG = "httpClient";

                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request.Builder ongoing = chain.request().newBuilder();
                        ongoing.addHeader("Accept", "application/json;versions=1");
                        //from preferences
                        if (preferences.isUserLoggedIn()) {
                            Log.d(TAG, "intercept: inside");
                            ongoing.addHeader("Authorization", "Bearer " + preferences.getToken());
                        }
                        return chain.proceed(ongoing.build());
                    }
                })
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120,TimeUnit.SECONDS).build();

        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .client(httpClient)
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}