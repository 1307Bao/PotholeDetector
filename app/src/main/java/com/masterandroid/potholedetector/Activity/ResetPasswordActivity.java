package com.masterandroid.potholedetector.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.common.api.Api;
import com.google.android.material.textfield.TextInputEditText;
import com.masterandroid.potholedetector.API.ApiClient;
import com.masterandroid.potholedetector.API.ApiService;
import com.masterandroid.potholedetector.API.DTO.Request.ApiResponse;
import com.masterandroid.potholedetector.API.DTO.Request.ResetPasswordRequest;
import com.masterandroid.potholedetector.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordActivity extends BaseActivity {

    private TextInputEditText textInputNewPassword, textInputConfirmPassword;
    private AppCompatButton btnReset;
    private ApiService apiService;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        apiService = ApiClient.getClient().create(ApiService.class);
        email = getIntent().getStringExtra("GMAIL");

        textInputNewPassword = findViewById(R.id.editResetPassword_Password);
        textInputConfirmPassword = findViewById(R.id.editResetPassword_ConfirmPassword);
        Toolbar toolbarResetPassword = findViewById(R.id.toolbarResetPassword);

        setUpBtnReset();

        toolbarResetPassword.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void setUpBtnReset() {
        btnReset = findViewById(R.id.btnResetNext);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPassword = textInputNewPassword.getText().toString();
                String confirmPassword = textInputConfirmPassword.getText().toString();

                if (newPassword.length() < 8) {
                    Toast.makeText(ResetPasswordActivity.this, "Password at least 8 character", Toast.LENGTH_LONG);
                } else if (!newPassword.equals(confirmPassword)) {
                    Toast.makeText(ResetPasswordActivity.this, "Please check confirm password again", Toast.LENGTH_LONG);
                } else {
                    resetPassword();
                }
            }
        });
    }

    private void resetPassword() {
        String password = textInputNewPassword.getText().toString();
        Log.e("BODY", email + " " + password);
        ResetPasswordRequest request = new ResetPasswordRequest(email, password);
        Call<ApiResponse<String>> reset = apiService.resetPassword(request);
        
        reset.enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                if (response.body().getCode() == 200) {
                    Intent intent = new Intent(ResetPasswordActivity.this, SignInActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(ResetPasswordActivity.this, "I don't know why", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable throwable) {
                Toast.makeText(ResetPasswordActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}