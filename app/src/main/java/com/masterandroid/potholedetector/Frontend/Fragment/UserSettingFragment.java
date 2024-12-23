package com.masterandroid.potholedetector.Frontend.Fragment;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.masterandroid.potholedetector.Frontend.API.ApiClient;
import com.masterandroid.potholedetector.Frontend.API.ApiService;
import com.masterandroid.potholedetector.Frontend.API.DTO.Request.ApiResponse;
import com.masterandroid.potholedetector.Frontend.Security.SecureStorage;
import com.masterandroid.potholedetector.R;

import java.io.IOException;
import java.security.GeneralSecurityException;

import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserSettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserSettingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserSettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserSettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserSettingFragment newInstance(String param1, String param2) {
        UserSettingFragment fragment = new UserSettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private EditText etName, etUsername;
    private Button btnSend;
    private Toolbar toolbar;
    private ApiService apiService;
    private SecureStorage secureStorage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_setting, container, false);

        etName = view.findViewById(R.id.settingName);
        etUsername = view.findViewById(R.id.settingUsername);
        btnSend = view.findViewById(R.id.btnSettingSend);
        toolbar = view.findViewById(R.id.userToolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.remove(UserSettingFragment.this);
                Fragment fragment = fragmentManager.findFragmentByTag("PROFILE");
                assert fragment != null;
                fragmentTransaction.show(fragment);
                fragmentTransaction.commit();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (etName.getText().toString().isEmpty() || etUsername.getText().toString().isEmpty()) {
                        return;
                    }
                    handleUpdateUser();
                } catch (GeneralSecurityException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        return view;
    }

    private void handleUpdateUser() throws GeneralSecurityException, IOException {
        secureStorage = new SecureStorage(getContext());
        String token = secureStorage.getValue("TOKEN_FLAG");
        apiService = ApiClient.getClientWithToken(token).create(ApiService.class);
        Call<ApiResponse<Void>> call = apiService.updateUser(etUsername.getText().toString(), etName.getText().toString());
        call.enqueue(new retrofit2.Callback<ApiResponse<Void>>() {

            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful()) {
                    secureStorage.save(token, etName.getText().toString(), etUsername.getText(). toString());
                    getActivity().recreate();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.remove(UserSettingFragment.this);
                    Fragment fragment = fragmentManager.findFragmentByTag("PROFILE");
                    assert fragment != null;
                    fragmentTransaction.show(fragment);
                    fragmentTransaction.commit();
                } else {
                    if (response.code() == 400) {
                        Toast.makeText(requireContext(), "USERNAME EXISTED", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable throwable) {
                Toast.makeText(requireContext(), "Try Again", Toast.LENGTH_SHORT).show();
            }
        });
    }
}