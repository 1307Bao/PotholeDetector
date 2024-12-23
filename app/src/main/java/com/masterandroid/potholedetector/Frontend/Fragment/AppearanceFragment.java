package com.masterandroid.potholedetector.Frontend.Fragment;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.masterandroid.potholedetector.Frontend.Helper.ThemeHelper;
import com.masterandroid.potholedetector.R;

public class AppearanceFragment extends Fragment {
    private RadioGroup radioGroup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appearance, container, false);

        Toolbar toolbar = view.findViewById(R.id.appearanceToolbar);
        radioGroup = view.findViewById(R.id.appearanceRadioGroup);

        // Set current theme selection
        String currentTheme = ThemeHelper.getThemeMode(requireContext());
        switch (currentTheme) {
            case "light":
                radioGroup.check(R.id.appearanceRadioButtonLight);
                break;
            case "dark":
                radioGroup.check(R.id.appearanceRadioButtonDark);
                break;
            default:
                radioGroup.check(R.id.appearanceRadioButtonSystem);
                break;
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                // Remove this fragment and show profile fragment
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.remove(AppearanceFragment.this);
                Fragment profileFragment = fragmentManager.findFragmentByTag("PROFILE");
                if (profileFragment != null) {
                    fragmentTransaction.show(profileFragment);
                }
                fragmentTransaction.commit();
            }
        });

        // Handle theme changes
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            String selectedTheme;
            if (checkedId == R.id.appearanceRadioButtonLight) {
                selectedTheme = "light";
            } else if (checkedId == R.id.appearanceRadioButtonDark) {
                selectedTheme = "dark";
            } else {
                selectedTheme = "system";
            }

            ThemeHelper.saveThemeMode(requireContext(), selectedTheme);
            ThemeHelper.applyTheme(selectedTheme);

            // Clean up MapboxNavigation before recreation
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            Fragment mapFragment = fragmentManager.findFragmentByTag("MAP");
            if (mapFragment instanceof MapFragment) {
                ((MapFragment) mapFragment).cleanupMapboxNavigation();
            }

            // Remove this fragment and show profile fragment
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(this);
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