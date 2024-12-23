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

        // Only create new fragments if this is not a recreation
        if (savedInstanceState == null) {
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
        } else {
            // Restore fragments from saved state
            homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("HOME");
            mapFragment = (MapFragment) getSupportFragmentManager().findFragmentByTag("MAP");
            reportFragment = (ReportFragment) getSupportFragmentManager().findFragmentByTag("REPORT");
            profileFragment = (ProfileFragment) getSupportFragmentManager().findFragmentByTag("PROFILE");

            // Find which fragment was active
            if (homeFragment != null && !homeFragment.isHidden()) activeFragment = homeFragment;
            else if (mapFragment != null && !mapFragment.isHidden()) activeFragment = mapFragment;
            else if (reportFragment != null && !reportFragment.isHidden()) activeFragment = reportFragment;
            else if (profileFragment != null && !profileFragment.isHidden()) activeFragment = profileFragment;
        }

        setUpNavBottom();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clean up MapboxNavigation when activity is destroyed
        if (mapFragment != null) {
            mapFragment.cleanupMapboxNavigation();
        }
    }

    private void setUpNavBottom() {
        navBottom.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                    String tag = fragment.getTag();
                    if (tag == null || (!tag.equals("HOME") && !tag.equals("MAP") &&
                            !tag.equals("REPORT") && !tag.equals("PROFILE"))) {
                        transaction.remove(fragment);
                    }
                }

                if (item.getItemId() == R.id.home) {
                    if (!(activeFragment instanceof HomeFragment)) {
                        transaction.hide(activeFragment).show(homeFragment).commit();
                        activeFragment = homeFragment;
                    } else {
                        homeFragment = new HomeFragment();
                        transaction.hide(activeFragment)
                                .remove(activeFragment)
                                .add(R.id.fragmentContainer, homeFragment, "HOME")
                                .commit();
                        activeFragment = homeFragment;
                    }
                    return true;
                } else if (item.getItemId() == R.id.map) {
                    if (!(activeFragment instanceof MapFragment)) {
                        transaction.hide(activeFragment).show(mapFragment).commit();
                        activeFragment = mapFragment;
                    } else {
                        // Clean up previous MapFragment if it exists
                        if (mapFragment != null) {
                            mapFragment.cleanupMapboxNavigation();
                            transaction.remove(mapFragment);
                        }
                        mapFragment = new MapFragment();
                        transaction.hide(activeFragment)
                                .add(R.id.fragmentContainer, mapFragment, "MAP")
                                .commit();
                        activeFragment = mapFragment;
                    }
                    return true;
                } else if (item.getItemId() == R.id.report) {
                    if (!(activeFragment instanceof ReportFragment)) {
                        transaction.hide(activeFragment).show(reportFragment).commit();
                        activeFragment = reportFragment;
                    } else {
                        reportFragment = new ReportFragment();
                        transaction.hide(activeFragment)
                                .remove(activeFragment)
                                .add(R.id.fragmentContainer, reportFragment, "REPORT")
                                .commit();
                        activeFragment = reportFragment;
                    }
                    return true;
                } else if (item.getItemId() == R.id.profile) {
                    if (!(activeFragment instanceof ProfileFragment)) {
                        transaction.hide(activeFragment).show(profileFragment).commit();
                        activeFragment = profileFragment;
                    }
                    return true;
                }
                return false;
            }
        });
    }
}