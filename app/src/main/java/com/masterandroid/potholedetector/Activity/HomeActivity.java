package com.masterandroid.potholedetector.Activity;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.masterandroid.potholedetector.Fragment.HomeFragment;
import com.masterandroid.potholedetector.Fragment.MapFragment;
import com.masterandroid.potholedetector.Fragment.ProfileFragment;
import com.masterandroid.potholedetector.Fragment.ReportFragment;
import com.masterandroid.potholedetector.R;


public class HomeActivity extends BaseActivity {

    private BottomNavigationView navBottom;

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
        switchFragment(new HomeFragment());

        setUpNavBottom();
    }

    private void switchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.commit();
    }

    private void setUpNavBottom() {
        navBottom.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment currentFragment = getCurrentFragment();
                if (item.getItemId() == R.id.home) {
                    switchFragment(new HomeFragment());
                    return true;
                } else if (item.getItemId() == R.id.map && !(currentFragment instanceof MapFragment)) {
                    switchFragment(new MapFragment());
                    return true;
                } else if (item.getItemId() == R.id.report) {
                    switchFragment(new ReportFragment());
                    return true;
                } else if (item.getItemId() == R.id.profile) {
                    switchFragment(new ProfileFragment());
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

    public Fragment getCurrentFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        return fragmentManager.findFragmentById(R.id.fragmentContainer);
    }


}