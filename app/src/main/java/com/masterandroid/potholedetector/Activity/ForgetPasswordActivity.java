package com.masterandroid.potholedetector.Activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.masterandroid.potholedetector.API.ApiClient;
import com.masterandroid.potholedetector.API.ApiService;
import com.masterandroid.potholedetector.API.DTO.Request.ApiResponse;
import com.masterandroid.potholedetector.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPasswordActivity extends BaseActivity {

    private AppCompatButton btnNextToEnterOTP;
    private TextInputEditText emailText;
    private TextInputLayout emailLayout;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forget_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        apiService = ApiClient.getClient().create(ApiService.class);

        emailText = findViewById(R.id.editForgetPasswordEmail);
        btnNextToEnterOTP = findViewById(R.id.btnForgetPasswordNext);
        emailLayout = findViewById(R.id.emailLayout);

        Toolbar toolbarForgetPassword = findViewById(R.id.toolBarForgetPassword);

        setUpBtnSendOTP();

        toolbarForgetPassword.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void setUpBtnSendOTP() {
        btnNextToEnterOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailText.getText().toString();
                if (email.contains("@gmail.com")){
                    Call<ApiResponse<String>> getOTP = apiService.getOTPForResetPassword(email);

                    getOTP.enqueue(new Callback<ApiResponse<String>>() {
                        @Override
                        public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                            if (response.body().getCode() == 1005) {
                                emailLayout.setHelperTextEnabled(true);
                                emailLayout.setHelperText("Wrong email");
                                emailLayout.setHelperTextColor(ColorStateList.valueOf(
                                        Color.parseColor("#FF0000")));
                            } else {
                                Intent intent = new Intent(ForgetPasswordActivity.this
                                        , EnterOTPActivity.class);
                                intent.putExtra("GMAIL", email);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse<String>> call, Throwable throwable) {
                            Toast.makeText(ForgetPasswordActivity.this, "Connection Failed", Toast.LENGTH_LONG);
                        }
                    });
                } else {
                    Toast.makeText(ForgetPasswordActivity.this, "Please enter your email", Toast.LENGTH_LONG);
                }
            }
        });
    }
}