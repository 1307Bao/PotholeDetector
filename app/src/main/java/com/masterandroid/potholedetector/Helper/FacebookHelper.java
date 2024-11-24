package com.masterandroid.potholedetector.Helper;

import android.app.Activity;
import android.content.Intent;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;

public class FacebookHelper {
    private static FacebookHelper instance;
    private CallbackManager callbackManager;

    private FacebookHelper() {
        FacebookSdk.sdkInitialize(FacebookSdk.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
    }

    public static FacebookHelper getInstance() {
        if (instance == null) {
            instance = new FacebookHelper();
        }
        return instance;
    }

    public CallbackManager getCallbackManager() {
        return callbackManager;
    }

    public void login(Activity activity) {
        LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("email", "public_profile"));
    }

    public void handleActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void registerCallback(Activity activity, FacebookCallback<LoginResult> callback) {
        LoginManager.getInstance().registerCallback(callbackManager, callback);
    }
}
