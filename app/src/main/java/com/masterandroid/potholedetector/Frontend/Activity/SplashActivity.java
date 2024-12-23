package com.masterandroid.potholedetector.Frontend.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;

import com.masterandroid.potholedetector.Frontend.API.ApiClient;
import com.masterandroid.potholedetector.Frontend.API.ApiService;
import com.masterandroid.potholedetector.Frontend.API.DTO.Request.ApiResponse;
import com.masterandroid.potholedetector.Frontend.API.DTO.Request.IntrospectRequest;
import com.masterandroid.potholedetector.Frontend.API.DTO.Response.AuthenticationResponse;
import com.masterandroid.potholedetector.Frontend.Security.SecureStorage;
import com.masterandroid.potholedetector.R;
import com.masterandroid.potholedetector.Frontend.Helper.LocaleHelper;

import java.io.IOException;
import java.security.GeneralSecurityException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SplashActivity extends BaseActivity {
    private static final String IS_CHOOSE_LANGUAGE_FIRST = "is_choose_language";
    private static final String TOKEN_FLAG = "TOKEN_FLAG";
    private ApiService apiService;
    private SecureStorage secureStorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        apiService = ApiClient.getClient().create(ApiService.class);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    secureStorage = new SecureStorage(SplashActivity.this);
                    String token = secureStorage.getToken(TOKEN_FLAG);

                    if (!token.isEmpty()) {
                        Call<ApiResponse<AuthenticationResponse>> introspect = apiService.introspect(
                                new IntrospectRequest(token)
                        );
                        introspect.enqueue(new Callback<ApiResponse<AuthenticationResponse>>() {
                            @Override
                            public void onResponse(Call<ApiResponse<AuthenticationResponse>> call, Response<ApiResponse<AuthenticationResponse>> response) {
                                boolean isValid = false;
                                if (response.isSuccessful()) {
                                    assert response.body() != null;
                                    Log.e("TOKEN FL 1", response.body().getResult().isValid() + "");
                                    if (response.body().getResult().isValid()) {
                                        isValid = true;
                                    }
                                }
                                navigateToNextScreen(isValid);
                            }

                            @Override
                            public void onFailure(Call<ApiResponse<AuthenticationResponse>> call, Throwable throwable) {
                                navigateToNextScreen(false);
                            }
                        });
                    } else {
                        navigateToNextScreen(false);
                    }
                } catch (GeneralSecurityException | IOException e) {
                    navigateToNextScreen(false);
                }
            }
        }, 2000);
    }

    private void navigateToNextScreen(boolean isValid) {
        String language = LocaleHelper.getLanguage(SplashActivity.this);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this);
        boolean isChoose =  preferences.getBoolean(IS_CHOOSE_LANGUAGE_FIRST, false);

        if (!isChoose) {
            Intent intent = new Intent(SplashActivity.this, LanguageChooserActivity.class);
            startActivity(intent);
        } else {
            boolean isChooseLanguage = LocaleHelper.getLanguage(this) != null;

            if (isChooseLanguage) {
                if (isValid) {
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
            Intent intent = new Intent(SplashActivity.this, CreateAccountActivity.class);
            startActivity(intent);
        }
        finish();
    }
}