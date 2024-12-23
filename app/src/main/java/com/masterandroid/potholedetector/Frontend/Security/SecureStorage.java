package com.masterandroid.potholedetector.Frontend.Security;

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
    private static final String NAME_FLAG = "NAME_FLAG";
    private static final String USERNAME_FLAG = "USERNAME_FLAG";

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

    public void save(String token, String name, String username) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TOKEN_FLAG, token);
        editor.putString(NAME_FLAG, name);
        editor.putString(USERNAME_FLAG, username);
        editor.apply();
    }

    public String getValue(String flags) {
        return sharedPreferences.getString(flags, "");
    }

    public void deleteToken(String flags) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(flags);
        editor.apply();
    }
}
