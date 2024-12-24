package com.masterandroid.potholedetector.Frontend.API;

import android.util.Log;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String url = "https://fc6a-14-169-91-230.ngrok-free.app";

    public static Retrofit getClient() {
        return new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static Retrofit getClientWithToken(String token) {
        Log.e("TOKEN IN CLIENT:", token);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor(token))
                .build();

        return new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }


}

