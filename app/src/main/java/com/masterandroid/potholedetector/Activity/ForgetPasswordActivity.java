package com.masterandroid.potholedetector.Activity;

import android.content.Intent;
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
import com.masterandroid.potholedetector.R;

public class ForgetPasswordActivity extends BaseActivity {

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

        TextInputEditText emailText = findViewById(R.id.editForgetPasswordEmail);
        AppCompatButton btnNextToEnterOTP = findViewById(R.id.btnForgetPasswordNext);
        Toolbar toolbarForgetPassword = findViewById(R.id.toolBarForgetPassword);

        btnNextToEnterOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emailText.getText().toString().contains("@gmail.com")){
                    Intent intent = new Intent(ForgetPasswordActivity.this, EnterOTPActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(ForgetPasswordActivity.this, "Please enter your email", Toast.LENGTH_LONG);
                }
            }
        });

        toolbarForgetPassword.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}