package com.masterandroid.potholedetector.Activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.masterandroid.potholedetector.Helper.LocaleHelper;
import com.masterandroid.potholedetector.R;

public class CreateAccountActivity extends AppCompatActivity {

    private TextView tvPrivacy, tvSignIn;
    private TextInputEditText textInputPassword, textInputName, textInputMail;
    private AppCompatButton btnNext;
    private LocaleHelper localeHelper;

    private void updateLanguage() {
        String language =  localeHelper.getLanguage(this);
        if (language != null) {
            localeHelper.setLocale(this, language);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        localeHelper = new LocaleHelper();
        updateLanguage();

        tvPrivacy = findViewById(R.id.privacy);
        tvSignIn = findViewById(R.id.signInAccount);
        textInputPassword = findViewById(R.id.editPassword);
        textInputMail = findViewById(R.id.editEmail);
        textInputName = findViewById(R.id.editName);
        btnNext = findViewById(R.id.btnCreateAccountNext);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateAccountActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

//      Create a link at a sequence
        setLinkText("Terms of Service", tvPrivacy);
        setLinkText("Privacy\n Policy", tvPrivacy);
        setLinkText("Sign In", tvSignIn);

//      Setting color for icon at the end of edit text
        TextInputLayout passwordLayout = findViewById(R.id.passwordLayout);
        passwordLayout.setEndIconTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.light_sub_background_color_gray)));

    }

    private void setLinkText(String word, TextView tv) {
        String text = tv.getText().toString();
        int startIndex = text.indexOf(word);

        if (startIndex != -1) {
            int endIndex = startIndex + word.length();
            SpannableString spannableString = new SpannableString(text);

            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#3461FD")),
                    startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            if (word.equals("Sign In")) {
                spannableString.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View view) {
                        Intent intent = new Intent(CreateAccountActivity.this, SignInActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setColor(Color.parseColor("#3461FD")); // Đặt màu cho chữ
                        ds.setUnderlineText(false); // Nếu không muốn gạch chân chữ
                    }

                }, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);



                tv.setText(spannableString);
                tv.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());
                return;
            }

            tv.setText(spannableString);
        }
    }

}
