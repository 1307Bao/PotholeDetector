package com.masterandroid.potholedetector.Frontend.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.masterandroid.potholedetector.Frontend.API.ApiClient;
import com.masterandroid.potholedetector.Frontend.API.ApiService;
import com.masterandroid.potholedetector.Frontend.API.DTO.Request.ApiResponse;
import com.masterandroid.potholedetector.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnterOTPActivity extends BaseActivity {
    private EditText otpText1, otpText2, otpText3, otpText4, otpText5;
    private EditText[] editTexts;
    private AppCompatButton btnNext;
    private ApiService apiService;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_enter_otpactivity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        apiService = ApiClient.getClient().create(ApiService.class);
        email = getIntent().getStringExtra("GMAIL");

        btnNext = findViewById(R.id.btnEnterOTPNext);

        Toolbar toolbarEnterOTP = findViewById(R.id.toolbarEnterOTP);

        otpText1 = findViewById(R.id.otp_edit_1);
        otpText2 = findViewById(R.id.otp_edit_2);
        otpText3 = findViewById(R.id.otp_edit_3);
        otpText4 = findViewById(R.id.otp_edit_4);
        otpText5 = findViewById(R.id.otp_edit_5);

        editTexts = new EditText[]{otpText1, otpText2, otpText3, otpText4, otpText5};
        setUpEditTexts(editTexts);

        setUpBtnEnterOtp();

        toolbarEnterOTP.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void setUpEditTexts(EditText[] editTexts) {
        for (int i = 0; i < editTexts.length; i++) {
            int index = i;
            editTexts[i].setBackgroundColor(ContextCompat.getColor(this, R.color.light_sub_background_color_blue));
            editTexts[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (editable.length() == 1 && index < editTexts.length - 1) {
                        editTexts[index + 1].requestFocus();
                    }
                }
            });
        }
    }

    private void setUpBtnEnterOtp() {
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder otpBuilder = new StringBuilder("");
                for (int i = 0; i < editTexts.length; i++) {
                    otpBuilder.append(editTexts[i].getText().toString());
                }

                String otp = otpBuilder.toString();

                Log.e("OTP: ", otp);

                Call<ApiResponse<String>> validate = apiService.validateOtp(email, otp);
                Log.e("BODY", email + ":" + otp);
                validate.enqueue(new Callback<ApiResponse<String>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {

                            ApiResponse<String> apiResponse = response.body();
                            if (apiResponse != null) {
                                if (apiResponse.getCode() == 1022) {
                                    Toast.makeText(EnterOTPActivity.this, apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                                } else {
                                    Intent intent = new Intent(EnterOTPActivity.this, ResetPasswordActivity.class);
                                    intent.putExtra("GMAIL", email);
                                    startActivity(intent);
                                }
                            }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<String>> call, Throwable throwable) {

                    }
                });

            }
        });
    }
}