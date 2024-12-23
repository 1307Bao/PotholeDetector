package com.masterandroid.potholedetector.Frontend.Helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatDelegate;

public class ThemeHelper {
    private static final String THEME_PREF = "theme_mode";

    public static void applyTheme(String themePref) {
        switch (themePref) {
            case "light":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case "dark":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            default:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }
    }

    public static void saveThemeMode(Context context, String themeMode) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(THEME_PREF, themeMode);
        editor.apply();
    }

    public static String getThemeMode(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(THEME_PREF, "system");
    }
}