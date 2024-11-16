package com.masterandroid.potholedetector.Fragment;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import java.util.Locale;

import com.masterandroid.potholedetector.Helper.LocaleHelper;
import com.masterandroid.potholedetector.R;


public class LanguageFragment extends Fragment {

    private RadioGroup languageRadioGroup;
    private RadioButton languageRadioButtonEnglish, languageRadioButtonVietnamese;
    private Toolbar languageToolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_language, container, false);

        languageToolbar = view.findViewById(R.id.languageToolbar);
        languageRadioGroup = view.findViewById(R.id.languageRadioGroup);
        languageRadioButtonEnglish = view.findViewById(R.id.languageRadioButtonEnglish);
        languageRadioButtonVietnamese = view.findViewById(R.id.languageRadioButtonVietnamese);

        languageToolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());

        // Set current language selection
        String currentLanguage = LocaleHelper.getLanguage(requireContext());
        if (currentLanguage.equals("vi")) {
            languageRadioButtonVietnamese.setChecked(true);
        } else {
            languageRadioButtonEnglish.setChecked(true);
        }

        languageRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            String languageCode = "en";
            if (checkedId == R.id.languageRadioButtonVietnamese) {
                languageCode = "vi";
            }

            Log.e("Language", languageCode);

            setLocale(requireActivity(), languageCode);
        });

        return view;
    }


    private void setLocale(Context context, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = context.getResources();
        Configuration config = new Configuration(resources.getConfiguration());
        config.setLocale(locale);
        context.createConfigurationContext(config);
        resources.updateConfiguration(config, resources.getDisplayMetrics());

        // Lưu ngôn ngữ đã chọn
        context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
            .edit()
            .putString("language", languageCode)
            .apply();

        // Khởi động lại activity để áp dụng ngôn ngữ mới
        Intent intent = requireActivity().getIntent();
        requireActivity().finish();
        startActivity(intent);
    }
}