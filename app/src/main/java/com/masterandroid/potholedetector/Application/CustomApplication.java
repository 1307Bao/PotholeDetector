package com.masterandroid.potholedetector.Application;

import android.app.Application;
import android.content.Context;

import com.masterandroid.potholedetector.Helper.LocaleHelper;
 import com.masterandroid.potholedetector.Helper.ThemeHelper;

public class CustomApplication extends Application {
      @Override
     public void onCreate() {
         super.onCreate();
        
         // Apply saved theme
         String savedTheme = ThemeHelper.getThemeMode(this);
         ThemeHelper.applyTheme(savedTheme);
     }

    @Override
    protected void attachBaseContext(Context base) {
        // Lấy ngôn ngữ đã lưu và cập nhật Context
        String language = LocaleHelper.getLanguage(base);
        super.attachBaseContext(LocaleHelper.updateResources(base, language));
    }
}
