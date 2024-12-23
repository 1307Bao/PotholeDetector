package com.masterandroid.potholedetector.Frontend.Activity;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.masterandroid.potholedetector.Frontend.Fragment.HomeFragment;
import com.masterandroid.potholedetector.Frontend.Fragment.MapFragment;
import com.masterandroid.potholedetector.Frontend.Fragment.ProfileFragment;
import com.masterandroid.potholedetector.Frontend.Fragment.ReportFragment;
import com.masterandroid.potholedetector.R;


public class HomeActivity extends BaseActivity {

    private BottomNavigationView navBottom;
    private HomeFragment homeFragment;
    private MapFragment mapFragment;
    private ReportFragment reportFragment;
    private ProfileFragment profileFragment;

    private Fragment activeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        navBottom = findViewById(R.id.bottom_navigation);

        homeFragment = new HomeFragment();
        mapFragment = new MapFragment();
        reportFragment = new ReportFragment();
        profileFragment = new ProfileFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragmentContainer, profileFragment, "PROFILE").hide(profileFragment);
        transaction.add(R.id.fragmentContainer, reportFragment, "REPORT").hide(reportFragment);
        transaction.add(R.id.fragmentContainer, mapFragment, "MAP").hide(mapFragment);
        transaction.add(R.id.fragmentContainer, homeFragment, "HOME");
        transaction.commit();
        
        activeFragment = homeFragment;

        setUpNavBottom();
    }

    private void setUpNavBottom() {
        navBottom.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                if (item.getItemId() == R.id.home) {
                    if (!(activeFragment instanceof HomeFragment)) {
                        transaction.hide(activeFragment).show(homeFragment).commit();
                        activeFragment = homeFragment;
                    } else {
                        transaction.remove(homeFragment);
                        homeFragment.onDestroy();
                        homeFragment = new HomeFragment();
                        transaction.add(R.id.fragmentContainer, homeFragment, "HOME");
                        transaction.commit();
                        activeFragment = homeFragment;
                    }
                    return true;

                } else if (item.getItemId() == R.id.map ) {
                    if (!(activeFragment instanceof MapFragment)) {
                        transaction.hide(activeFragment).show(mapFragment).commit();
                        activeFragment = mapFragment;
                    } else {
                        mapFragment.onDestroy();
                        transaction.remove(mapFragment);
                        mapFragment = new MapFragment();
                        transaction.add(R.id.fragmentContainer, mapFragment, "MAP");
                        transaction.commit();
                        activeFragment = mapFragment;
                    }
                    return true;

                } else if (item.getItemId() == R.id.report) {
                    if (!(activeFragment instanceof ReportFragment)) {
                        transaction.hide(activeFragment).show(reportFragment).commit();
                        activeFragment = reportFragment;
                    } else {
                        transaction.remove(reportFragment);
                        reportFragment.onDestroy();
                        reportFragment = new ReportFragment();
                        transaction.add(R.id.fragmentContainer, reportFragment, "REPORT");
                        transaction.commit();
                        activeFragment = reportFragment;
                    }

                    return true;

                } else if (item.getItemId() == R.id.profile) {
                    if (!(activeFragment instanceof ProfileFragment)) {
                        transaction.hide(activeFragment).show(profileFragment).commit();
                        activeFragment = profileFragment;
                    } else {
                        transaction.remove(profileFragment);
                        transaction.add(R.id.fragmentContainer, profileFragment, "PROFILE");
                        transaction.commit();
                    }

                    return true;
                }

                return false;
            }
        });

        ColorStateList colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_checked}, // trạng thái được chọn
                        new int[]{-android.R.attr.state_checked} // trạng thái không được chọn
                },
                new int[]{
                        ContextCompat.getColor(this, R.color.light_primary_color), // Màu khi được chọn
                        ContextCompat.getColor(this, R.color.light_main_text_color) // Màu khi không được chọn
                }
        );

        navBottom.setItemIconTintList(colorStateList);
        navBottom.setItemTextColor(colorStateList);
    }

}