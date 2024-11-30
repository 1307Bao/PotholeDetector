package com.masterandroid.potholedetector.Security;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class SecureStorage {
    private SharedPreferences sharedPreferences;
    private static final String TOKEN_FLAG = "TOKEN_FLAG";

    @RequiresApi(api = Build.VERSION_CODES.M)
    public SecureStorage(Context context) throws GeneralSecurityException, IOException {
        String masterKey = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);

        sharedPreferences = EncryptedSharedPreferences.create(
                "StorageToken",
                masterKey,
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );
    }

    public void saveToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TOKEN_FLAG, token);
        editor.apply();
    }

    public String getToken(String flags) {
        return sharedPreferences.getString(flags, "");
    }

    public void deleteToken(String flags) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(flags);
        editor.apply();
    }
}
