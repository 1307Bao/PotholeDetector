package com.masterandroid.potholedetector.Frontend.Fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.masterandroid.potholedetector.Frontend.API.ApiClient;
import com.masterandroid.potholedetector.Frontend.API.ApiService;
import com.masterandroid.potholedetector.Frontend.API.DTO.Request.ApiResponse;
import com.masterandroid.potholedetector.Frontend.API.DTO.Request.LogoutRequest;
import com.masterandroid.potholedetector.Frontend.API.DTO.Response.AuthenticationResponse;
import com.masterandroid.potholedetector.Frontend.Activity.CreateAccountActivity;
import com.masterandroid.potholedetector.Frontend.Adapter.SettingAdapter;
import com.masterandroid.potholedetector.Frontend.Event.SettingItemInteface;
import com.masterandroid.potholedetector.Frontend.Security.SecureStorage;
import com.masterandroid.potholedetector.R;

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
    private TextView tvName, tvUsername;
    private static final String NAME_FLAG = "NAME_FLAG";
    private static final String USERNAME_FLAG = "USERNAME_FLAG";
    private static final String TOKEN_FLAG = "TOKEN_FLAG";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        btnLogout = view.findViewById(R.id.profileButtonLogOut);
        apiService = ApiClient.getClient().create(ApiService.class);

        tvName = view.findViewById(R.id.profileUserName);
        tvUsername = view.findViewById(R.id.profileUserID);

        try {
            initData();
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
        setUpBtnLogout();
        RecyclerView recyclerView = view.findViewById(R.id.profileAllSetting);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        SettingAdapter adapter = new SettingAdapter(settingNameList, getContext(), this);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void initData() throws GeneralSecurityException, IOException {
        settingNameList = new ArrayList<String>();
        settingNameList.add(getString(R.string.setting_notifications));
        settingNameList.add(getString(R.string.setting_appearance));
        settingNameList.add(getString(R.string.setting_language));
        settingNameList.add(getString(R.string.setting_privacy));
        settingNameList.add(getString(R.string.setting_storage));

        SecureStorage secureStorage = new SecureStorage(requireContext());
        String name = secureStorage.getValue(NAME_FLAG);
        String username = secureStorage.getValue(USERNAME_FLAG);
        tvName.setText(name);
        tvUsername.setText(username);
    }

    @Override
    public void click(int position) {
        if (position == 0) {
            switchFragment(new NotificationFragment());
        } else if (position == 1) {
            switchFragment(new AppearanceFragment());
        } else if (position == 2) {
            switchFragment(new LanguageFragment());
        } else if (position == 3) {
            switchFragment(new UserSettingFragment());
        } else if (position == 4) {
            switchFragment(new SensorFragment());
        }
    }

    private void switchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(this);
        fragmentTransaction.add(R.id.fragmentContainer, fragment);
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
        String token = secureStorage.getValue(TOKEN_FLAG);

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