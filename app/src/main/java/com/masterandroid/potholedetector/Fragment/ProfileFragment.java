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
import android.widget.Toast;

import com.masterandroid.potholedetector.API.ApiClient;
import com.masterandroid.potholedetector.API.ApiService;
import com.masterandroid.potholedetector.API.DTO.Request.ApiResponse;
import com.masterandroid.potholedetector.API.DTO.Request.LogoutRequest;
import com.masterandroid.potholedetector.API.DTO.Response.AuthenticationResponse;
import com.masterandroid.potholedetector.Activity.CreateAccountActivity;
import com.masterandroid.potholedetector.Adapter.SettingAdapter;
import com.masterandroid.potholedetector.Event.SettingItemInteface;
import com.masterandroid.potholedetector.R;
import com.masterandroid.potholedetector.Security.SecureStorage;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileFragment extends Fragment implements SettingItemInteface {

    private static ArrayList<String> settingNameList;
    private AppCompatButton btnLogout;
    private ApiService apiService;
    private SecureStorage secureStorage;
    private static final String TOKEN_FLAG = "TOKEN_FLAG";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        btnLogout = view.findViewById(R.id.profileButtonLogOut);
        apiService = ApiClient.getClient().create(ApiService.class);

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
                try {
                    logout();
                } catch (GeneralSecurityException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });
    }

    private void logout() throws GeneralSecurityException, IOException {
        secureStorage = new SecureStorage(requireContext());
        String token = secureStorage.getToken(TOKEN_FLAG);

        LogoutRequest request = new LogoutRequest(token);

        Call<ApiResponse<AuthenticationResponse>> logout = apiService.logout(request);
        logout.enqueue(new Callback<ApiResponse<AuthenticationResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<AuthenticationResponse>> call, Response<ApiResponse<AuthenticationResponse>> response) {
                if (response.isSuccessful()) {
                    secureStorage.deleteToken(TOKEN_FLAG);

                    Intent intent = new Intent(getActivity(), CreateAccountActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                } else {
                    Toast.makeText(requireContext(), "Try Again!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<AuthenticationResponse>> call, Throwable throwable) {
                Toast.makeText(requireContext(), "Try Again!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}