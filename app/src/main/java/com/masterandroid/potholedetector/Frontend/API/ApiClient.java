package com.masterandroid.potholedetector.Frontend.API;

import android.util.Log;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String url = "https://a15d-2405-4803-c858-b610-902a-233f-6162-7ba6.ngrok-free.app";

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

