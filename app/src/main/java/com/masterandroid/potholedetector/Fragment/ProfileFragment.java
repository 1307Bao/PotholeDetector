package com.masterandroid.potholedetector.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.masterandroid.potholedetector.Activity.CreateAccountActivity;
import com.masterandroid.potholedetector.Adapter.SettingAdapter;
import com.masterandroid.potholedetector.Event.SettingItemInteface;
import com.masterandroid.potholedetector.R;

import java.util.ArrayList;


public class ProfileFragment extends Fragment implements SettingItemInteface {

    private static ArrayList<String> settingNameList;
    private AppCompatButton btnLogout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        btnLogout = view.findViewById(R.id.profileButtonLogOut);

        initData();
        setUpBtnLogout();
        RecyclerView recyclerView = view.findViewById(R.id.profileAllSetting);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        SettingAdapter adapter = new SettingAdapter(settingNameList, getContext(), this);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void initData() {
        settingNameList = new ArrayList<String>();
        settingNameList.add(getString(R.string.setting_notifications));
        settingNameList.add(getString(R.string.setting_appearance));
        settingNameList.add(getString(R.string.setting_language));
        settingNameList.add(getString(R.string.setting_privacy));
        settingNameList.add(getString(R.string.setting_storage));
    }

    @Override
    public void click(int position) {
        if (position == 0) {
            switchFragment(new NotificationFragment());
        } else if (position == 1) {
            switchFragment(new AppearanceFragment());
        } else if (position == 2) {
            switchFragment(new LanguageFragment());
        }
    }

    private void switchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.commit();
    }

    private void setUpBtnLogout() {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_logout, null);

        AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
        dialog.setView(view);

        // Loại bỏ padding mặc định
        dialog.setOnShowListener(dialogInterface -> {
            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            }
        });

        AppCompatButton btnCancel = view.findViewById(R.id.dialogButtonCancel);
        AppCompatButton btnAccept = view.findViewById(R.id.dialogButtonLogout);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreateAccountActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });
    }
}