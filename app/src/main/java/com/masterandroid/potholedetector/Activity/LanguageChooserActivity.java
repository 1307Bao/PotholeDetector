package com.masterandroid.potholedetector.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.masterandroid.potholedetector.Helper.LocaleHelper;
import com.masterandroid.potholedetector.R;

public class LanguageChooserActivity extends AppCompatActivity {

    private AppCompatButton btnEnglish, btnVietNamese, btnNext;
    private Boolean isClick = false;
    private LocaleHelper localeHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_language_chooser);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (LocaleHelper.getLanguage(this) != null) {
            Intent intent = new Intent(LanguageChooserActivity.this, CreateAccountActivity.class);
            startActivity(intent);
            finish();
        }

        localeHelper = new LocaleHelper();

         // Thay thế việc lấy màu trực tiếp bằng việc sử dụng theme attributes
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.subBackgroundColorGray, typedValue, true);
        int colorClicked = ContextCompat.getColor(this, typedValue.resourceId);
        
        getTheme().resolveAttribute(R.attr.subBackgroundColorBlue, typedValue, true);
        int colorDefault = ContextCompat.getColor(this, typedValue.resourceId);

        btnEnglish = findViewById(R.id.btnEnglishLanguage);
        btnVietNamese = findViewById(R.id.btnVietNamLanguage);
        btnNext = findViewById(R.id.btnLanguageNext);

        btnVietNamese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnVietNamese.setBackgroundColor(colorClicked);
                btnEnglish.setBackgroundColor(colorDefault);
                localeHelper.setLocale(LanguageChooserActivity.this, "vi");
                isClick = true;
            }
        });

        btnEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnEnglish.setBackgroundColor(colorClicked);
                btnVietNamese.setBackgroundColor(colorDefault);
                localeHelper.setLocale(LanguageChooserActivity.this, "en");

                isClick = true;
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isClick) {
                    Intent intent = new Intent(LanguageChooserActivity.this, CreateAccountActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LanguageChooserActivity.this, "Please choose language", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}