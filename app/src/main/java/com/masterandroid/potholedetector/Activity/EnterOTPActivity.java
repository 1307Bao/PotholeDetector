package com.masterandroid.potholedetector.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.masterandroid.potholedetector.R;

public class EnterOTPActivity extends BaseActivity {

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

        EditText otpText1, otpText2, otpText3, otpText4, otpText5;

        AppCompatButton btnNext = findViewById(R.id.btnEnterOTPNext);

        Toolbar toolbarEnterOTP = findViewById(R.id.toolbarEnterOTP);

        otpText1 = findViewById(R.id.otp_edit_1);
        otpText2 = findViewById(R.id.otp_edit_2);
        otpText3 = findViewById(R.id.otp_edit_3);
        otpText4 = findViewById(R.id.otp_edit_4);
        otpText5 = findViewById(R.id.otp_edit_5);

        EditText[] editTexts = new EditText[]{otpText1, otpText2, otpText3, otpText4, otpText5};
        setUpEditTexts(editTexts);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EnterOTPActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
            }
        });

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
}