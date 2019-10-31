package com.digital_easy.info_share.API;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInterface {

    private static final String CACHE_DIR = "brightly";
    // private static final String BASE_URL = "http://www.brightlyapp.com/api/"; //Production
    private static final String BASE_URL = "http://13.251.35.86/BrightlyDEV/api/";//Development
    //private static final String BASE_URL = "http://13.251.35.86/BrightlyUAT/api/";

    static Retrofit retrofit = null;


    public static RestApiMethods getRestApiMethods(Context context) {


        if (retrofit == null) {

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(1, TimeUnit.MINUTES)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build();

            // Retrofit setup
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))

                    .client(client)
                    .build();
            // Service setup


        }
        return retrofit.create(RestApiMethods.class);
    }


}
