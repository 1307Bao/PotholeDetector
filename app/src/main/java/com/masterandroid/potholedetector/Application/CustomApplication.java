package com.masterandroid.potholedetector.Application;

import android.app.Application;
import android.content.Context;

import com.masterandroid.potholedetector.Helper.LocaleHelper;

public class CustomApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        // Lấy ngôn ngữ đã lưu và cập nhật Context
        String language = LocaleHelper.getLanguage(base);
        super.attachBaseContext(LocaleHelper.updateResources(base, language));
    }
}
