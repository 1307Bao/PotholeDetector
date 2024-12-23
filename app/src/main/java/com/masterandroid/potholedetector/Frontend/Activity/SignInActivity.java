package com.masterandroid.potholedetector.Frontend.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.masterandroid.potholedetector.Frontend.API.ApiClient;
import com.masterandroid.potholedetector.Frontend.API.ApiService;
import com.masterandroid.potholedetector.Frontend.API.DTO.Request.ApiResponse;
import com.masterandroid.potholedetector.Frontend.API.DTO.Request.LoginByFacebookRequest;
import com.masterandroid.potholedetector.Frontend.API.DTO.Request.LoginByGmailRequest;
import com.masterandroid.potholedetector.Frontend.API.DTO.Request.LoginByUserRequest;
import com.masterandroid.potholedetector.Frontend.API.DTO.Response.LoginResponse;
import com.masterandroid.potholedetector.Frontend.Security.SecureStorage;
import com.masterandroid.potholedetector.Frontend.Helper.FacebookHelper;
import com.masterandroid.potholedetector.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.GeneralSecurityException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends BaseActivity {

    private AppCompatButton btnSignInByUser, btnSignInByGoogle, btnSignInByFacebook;
    private TextInputEditText textEmail, textPassword;
    private ApiService apiService;
    private GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN = 1;
    private FacebookHelper facebookHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        apiService = ApiClient.getClient().create(ApiService.class);
        facebookHelper = FacebookHelper.getInstance();

        TextView tvSignUp = findViewById(R.id.signUpAccount);

        btnSignInByUser = findViewById(R.id.btnSignInNext);
        btnSignInByGoogle = findViewById(R.id.btnSignInByGmail);
        btnSignInByFacebook = findViewById(R.id.btnSignInByFacebook);

        textEmail = findViewById(R.id.editSignInEmail);
        textPassword = findViewById(R.id.editSignInPassword);

        TextView tvNextToForgetPassword = findViewById(R.id.tvForgetPasswordNext);

        String signUpStr = getString(R.string.no_account_sign_up);

        setUpSignUp(tvSignUp, signUpStr);
        setUpBtnSignInByUser();
        setUpBtnSignInByGmail();
        setUpBtnSignInByFacebook();

        setUpNextToForgetPassword(tvNextToForgetPassword);
    }

    // Set a color for Sign Up and launch to Sign Up Activity when click on
    private void setUpSignUp(TextView tvSignUp, String signUp) {

        int startIndex = tvSignUp.getText().toString().indexOf(signUp);
        int endIndex = tvSignUp.getText().length();

        SpannableString spannableStringSignUp = new SpannableString(tvSignUp.getText());

        spannableStringSignUp.setSpan(new ForegroundColorSpan(Color.parseColor("#3461FD")),
                startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableStringSignUp.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent intent = new Intent(SignInActivity.this, CreateAccountActivity.class);
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#3461FD"));
                ds.setUnderlineText(false);
            }

        }, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvSignUp.setText(spannableStringSignUp);
        tvSignUp.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());
    }

    // Add event to Sign In button, launch MainActivity
    private void setUpBtnSignInByUser(){
        btnSignInByUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = textEmail.getText().toString().trim();
                String password = textPassword.getText().toString().trim();

                Log.e("DATA", email + "  " + password);

                LoginByUserRequest request = new LoginByUserRequest(email, password);
                loginByUserHandler(request);
            }
        });
    }

    private void loginByUserHandler(LoginByUserRequest request) {
        Call<ApiResponse<LoginResponse>> login = apiService.loginByUser(request);

        login.enqueue(new Callback<ApiResponse<LoginResponse>>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onResponse(Call<ApiResponse<LoginResponse>> call, Response<ApiResponse<LoginResponse>> response) {
                handlerResponse(response);
            }

            @Override
            public void onFailure(Call<ApiResponse<LoginResponse>> call, Throwable throwable) {
                makeToast("Please try again");
            }
        });
    }

    private void setUpBtnSignInByGmail() {
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.client_id))
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        btnSignInByGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = googleSignInClient.getSignInIntent();
                startActivityForResult(intent, RC_SIGN_IN);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loginByGoogle(requestCode, resultCode, data);
        loginByFacebook(requestCode, resultCode, data);
    }

    private void loginByGoogle(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    if (account.getIdToken() != null) {
                        Log.e("Google Token", account.getIdToken());
                        LoginByGmailRequest request = new LoginByGmailRequest(account.getIdToken());
                        sendLoginByGoogleRequest(request);
                    } else {
                        makeToast("Register Failed");
                    }
                } else {
                    makeToast("Register Failed");
                }

            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void sendLoginByGoogleRequest(LoginByGmailRequest request) {
        Call<ApiResponse<LoginResponse>> login = apiService.loginByGmail(request);

        login.enqueue(new Callback<ApiResponse<LoginResponse>>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onResponse(Call<ApiResponse<LoginResponse>> call,
                                   Response<ApiResponse<LoginResponse>> response) {
                handlerResponse(response);
            }

            @Override
            public void onFailure(Call<ApiResponse<LoginResponse>> call, Throwable t) {
                makeToast("Registration failed. Please try again later.");
            }
        });
    }

    private void setUpBtnSignInByFacebook() {
        facebookHelper.registerCallback(this, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                GraphRequest graphRequest = GraphRequest.newMeRequest(accessToken
                        , new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(@Nullable JSONObject jsonObject
                                    , @Nullable GraphResponse graphResponse) {
                                if (graphResponse.getError() != null) {
                                    throw new RuntimeException("Error fetching user data: "
                                            + graphResponse.getError().getErrorMessage());
                                } else {
                                    try {
                                        String name = jsonObject.getString("name");
                                        String email = jsonObject.getString("id");

                                        Log.e("USER DATA FB", name + " " + email);

                                        LoginByFacebookRequest request = new LoginByFacebookRequest(name, email);
                                        Call<ApiResponse<LoginResponse>> login = apiService.loginByFacebook(request);

                                        login.enqueue(new Callback<ApiResponse<LoginResponse>>() {
                                            @Override
                                            public void onResponse(Call<ApiResponse<LoginResponse>> call
                                                    , Response<ApiResponse<LoginResponse>> response) {

                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    handlerResponse(response);
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<ApiResponse<LoginResponse>> call, Throwable throwable) {
                                                makeToast("Register is Failed");
                                            }
                                        });
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            }
                        });

                graphRequest.executeAsync();
            }

            @Override
            public void onCancel() {
                makeToast("Register Canceled");
            }

            @Override
            public void onError(@NonNull FacebookException e) {
                makeToast("Register Failed");
            }
        });

        btnSignInByFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                facebookHelper.login(SignInActivity.this);
            }
        });
    }

    private void loginByFacebook(int requestCode, int resultCode, @Nullable Intent data) {
        facebookHelper.handleActivityResult(requestCode, resultCode, data);
    }



    // Set a event when click on to launch ForgetPassword Activity
    private void setUpNextToForgetPassword(TextView tvNextToForgetPassword) {
        tvNextToForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void handlerResponse(Response<ApiResponse<LoginResponse>> response) {
        ApiResponse<LoginResponse> apiResponse = response.body();
        LoginResponse loginResponse = (LoginResponse) apiResponse.getResult();

        try {
            SecureStorage secureStorage = new SecureStorage(getApplicationContext());
            secureStorage.saveToken(loginResponse.getToken());
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }


        Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void makeToast(String msg) {
        Toast.makeText(SignInActivity.this, msg, Toast.LENGTH_LONG).show();
    }
}