package com.masterandroid.potholedetector.Activity;

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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.masterandroid.potholedetector.API.ApiClient;
import com.masterandroid.potholedetector.API.ApiService;
import com.masterandroid.potholedetector.API.DTO.Request.ApiResponse;
import com.masterandroid.potholedetector.API.DTO.Request.LoginByUserRequest;
import com.masterandroid.potholedetector.API.DTO.Response.LoginResponse;
import com.masterandroid.potholedetector.API.DTO.Response.RegisterResponse;
import com.masterandroid.potholedetector.R;
import com.masterandroid.potholedetector.Security.SecureStorage;

import org.w3c.dom.Text;

import java.io.IOException;
import java.security.GeneralSecurityException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends BaseActivity {

    private AppCompatButton btnSignInByUser, btnSignInByGoogle, btnSignInByFacebook;
    private TextInputEditText textEmail, textPassword;
    private ApiService apiService;

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

        TextView tvSignUp = findViewById(R.id.signUpAccount);

        btnSignInByUser = findViewById(R.id.btnSignInNext);
        btnSignInByGoogle = findViewById(R.id.btnSignInByGmail);
        btnSignInByFacebook = findViewById(R.id.btnSignInByFacebook);

        textEmail = findViewById(R.id.editSignInEmail);
        textPassword = findViewById(R.id.editSignInPassword);

        TextView tvNextToForgetPassword = findViewById(R.id.tvForgetPasswordNext);

        String signUpStr = getString(R.string.no_account_sign_up);

        setUpSignUp(tvSignUp, signUpStr);
        setUpBtnSignInByUser(btnSignInByUser);

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
    private void setUpBtnSignInByUser(AppCompatButton button){
        button.setOnClickListener(new View.OnClickListener() {
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