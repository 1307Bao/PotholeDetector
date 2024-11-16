package com.masterandroid.potholedetector.Fragment;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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

import com.masterandroid.potholedetector.Activity.HomeActivity;
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

        languageToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.fragmentContainer, new ProfileFragment());
                fragmentTransaction.commit();
            }
        });

        // Set current language selection
        String currentLanguage = LocaleHelper.getLanguage(requireContext());

        Log.e("Language", currentLanguage);


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

            LocaleHelper.setLocale(getActivity(), languageCode);

            getActivity().recreate();
        });

        return view;
    }

}