package com.masterandroid.potholedetector.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.masterandroid.potholedetector.R;

import org.w3c.dom.Text;

public class SignInActivity extends BaseActivity {

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

        TextView tvSignUp = findViewById(R.id.signUpAccount);
        AppCompatButton button = findViewById(R.id.btnSignInNext);
        TextView tvNextToForgetPassword = findViewById(R.id.tvForgetPasswordNext);

        String signUpStr = getString(R.string.no_account_sign_up);

        setUpSignUp(tvSignUp, signUpStr);

        setUpEventButton(button);

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
    private void setUpEventButton(AppCompatButton button){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
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
}