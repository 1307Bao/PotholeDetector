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

public class ResetPasswordActivity extends BaseActivity {

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

        TextInputEditText textInputNewPassword = findViewById(R.id.editResetPassword_Password);
        TextInputEditText textInputConfirmPassword = findViewById(R.id.editResetPassword_ConfirmPassword);
        Toolbar toolbarResetPassword = findViewById(R.id.toolbarResetPassword);

        AppCompatButton btnReset = findViewById(R.id.btnResetNext);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPassword = textInputNewPassword.getText().toString();
                String confirmPassword = textInputConfirmPassword.getText().toString();

                if (!checkNewPassword(newPassword, confirmPassword)) {
                    Toast.makeText(ResetPasswordActivity.this, "Password at least 8 character", Toast.LENGTH_LONG);
                } else {
                    Intent intent = new Intent(ResetPasswordActivity.this, SignInActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });

        toolbarResetPassword.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private boolean checkNewPassword(String newPassword, String confirmPassword){
        if (newPassword.length() < 8) {
            return false;
        }

        if (!newPassword.equals(confirmPassword)) {
            return false;
        }

        return true;
    }
}