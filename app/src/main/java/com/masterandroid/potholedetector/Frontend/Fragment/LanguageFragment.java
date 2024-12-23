package com.masterandroid.potholedetector.Frontend.Fragment;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.masterandroid.potholedetector.Frontend.Helper.LocaleHelper;
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

        // Set up toolbar navigation
        languageToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.remove(LanguageFragment.this);

                Fragment profileFragment = fragmentManager.findFragmentByTag("PROFILE");
                assert profileFragment != null;
                fragmentTransaction.show(profileFragment);
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

        // Handle language changes
        languageRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            String languageCode = "en";
            if (checkedId == R.id.languageRadioButtonVietnamese) {
                languageCode = "vi";
            }

            Log.e("Language", languageCode);
            LocaleHelper.setLocale(getActivity(), languageCode);

            // Clean up MapboxNavigation before recreation
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            Fragment mapFragment = fragmentManager.findFragmentByTag("MAP");
            if (mapFragment instanceof MapFragment) {
                ((MapFragment) mapFragment).cleanupMapboxNavigation();
            }

            // Remove this fragment and show profile fragment
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(LanguageFragment.this);
            Fragment profileFragment = fragmentManager.findFragmentByTag("PROFILE");
            if (profileFragment != null) {
                fragmentTransaction.show(profileFragment);
            }
            fragmentTransaction.commit();

            // Recreate activity after fragment transaction
            requireActivity().recreate();
        });

        return view;
    }
}