package com.masterandroid.potholedetector.Activity;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.masterandroid.potholedetector.Helper.LocaleHelper;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
        String language = LocaleHelper.getLanguage(newBase);
        super.attachBaseContext(LocaleHelper.updateResources(newBase, language));
    }
}
