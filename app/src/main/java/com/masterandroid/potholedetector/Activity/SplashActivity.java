package com.masterandroid.potholedetector.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import com.masterandroid.potholedetector.R;
import com.masterandroid.potholedetector.Helper.LocaleHelper;


public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        String language = LocaleHelper.getLanguage(this);
        if (language == null || language.isEmpty()) {
            // Navigate to LanguageChooserActivity if language is not set
            Intent intent = new Intent(this, LanguageChooserActivity.class);
            startActivity(intent);
        } else {
            // Navigate to CreateAccountActivity if language is set
            Intent intent = new Intent(this, CreateAccountActivity.class);
            startActivity(intent);
        }
        finish();
    }

}