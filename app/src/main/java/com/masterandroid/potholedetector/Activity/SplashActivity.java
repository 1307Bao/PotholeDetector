package com.masterandroid.potholedetector.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import com.masterandroid.potholedetector.R;
import com.masterandroid.potholedetector.Helper.LocaleHelper;


public class SplashActivity extends BaseActivity {
    private static final String IS_CHOOSE_LANGUAGE = "is_choose_language";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Thêm handler để delay việc chuyển màn hình
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String language = LocaleHelper.getLanguage(SplashActivity.this);
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this);
                boolean isChoose =  preferences.getBoolean(IS_CHOOSE_LANGUAGE, false);

                if (!isChoose) {
                    Intent intent = new Intent(SplashActivity.this, LanguageChooserActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SplashActivity.this, CreateAccountActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        }, 2000); // Delay 2 giây
    }

}