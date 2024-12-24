package com.masterandroid.potholedetector.Frontend.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.masterandroid.potholedetector.Frontend.API.ApiClient;
import com.masterandroid.potholedetector.Frontend.API.ApiService;
import com.masterandroid.potholedetector.Frontend.API.DTO.Response.PotholeDetectedResponse;
import com.masterandroid.potholedetector.Frontend.API.DTO.Response.PotholeInfoResponse;
import com.masterandroid.potholedetector.Frontend.Activity.AllPotholesActivity;
import com.masterandroid.potholedetector.Frontend.Adapter.PotholeItemAdapter;
import com.masterandroid.potholedetector.Frontend.Model.PotholeModel;
import com.masterandroid.potholedetector.Frontend.Security.SecureStorage;
import com.masterandroid.potholedetector.R;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private ArrayList<PotholeModel> potholeModels;
    private static final int ALL_POTHOLES_REQUEST = 1;
    private PotholeItemAdapter adapter;
    private TextView tvAllPothole;
    private ApiService apiService;
    private RecyclerView recyclerView;
    private TextView tvTotalPothole, tvTotalPotholeEncountered, tvTodayPothole, tvHiUser;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        tvTotalPothole = view.findViewById(R.id.homeTotalPorhole);
        tvTotalPotholeEncountered = view.findViewById(R.id.distanceTotalPothole);
        tvTodayPothole = view.findViewById(R.id.homeTodayPotholes);
        recyclerView = view.findViewById(R.id.homeListPothole);
        tvHiUser = view.findViewById(R.id.hi_user);

        try {
            initData();
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }

        tvAllPothole = view.findViewById(R.id.homeAllPotholesNavigation);
        tvAllPothole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AllPotholesActivity.class);
                startActivityForResult(intent, ALL_POTHOLES_REQUEST);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ALL_POTHOLES_REQUEST && resultCode == Activity.RESULT_OK) {
            requireActivity().recreate();
        }
    }

    @SuppressLint("SetTextI18n")
    private void initData() throws GeneralSecurityException, IOException {
        potholeModels = new ArrayList<PotholeModel>();
        SecureStorage secureStorage = new SecureStorage(requireContext());
        String token = secureStorage.getValue("TOKEN_FLAG");
        String username = secureStorage.getValue("USERNAME_FLAG");
        tvHiUser.setText(tvHiUser.getText() + " " + username);
        apiService = ApiClient.getClientWithToken(token).create(ApiService.class);

        Call<PotholeInfoResponse> call = apiService.getMyPotholeInfo();
        call.enqueue(new Callback<PotholeInfoResponse>() {
            @Override
            public void onResponse(Call<PotholeInfoResponse> call, Response<PotholeInfoResponse> response) {
                if (response.isSuccessful()) {
                    PotholeInfoResponse potholeInfoResponse = response.body();
                    if (potholeInfoResponse != null) {
                        tvTotalPothole.setText(String.valueOf(potholeInfoResponse.getTotalPotholeDetected()));
                        tvTotalPotholeEncountered.setText(String.valueOf(potholeInfoResponse.getTotalPotholeEncounter()));
                        tvTodayPothole.setText(String.valueOf(potholeInfoResponse.getTodayPothole()));
                    }
                }
            }

            @Override
            public void onFailure(Call<PotholeInfoResponse> call, Throwable throwable) {

            }
        });

        Call<List<PotholeDetectedResponse>> call2 = apiService.getMyPotholeDetected();
        call2.enqueue(new Callback<List<PotholeDetectedResponse>>() {
            @Override
            public void onResponse(Call<List<PotholeDetectedResponse>> call, Response<List<PotholeDetectedResponse>> response) {
                if (response.isSuccessful()) {
                    List<PotholeDetectedResponse> potholeDetectedResponses = response.body();
                    if (potholeDetectedResponses != null) {
                        for (PotholeDetectedResponse potholeDetectedResponse : potholeDetectedResponses) {
                            PotholeModel potholeModel = new PotholeModel("DETECT", potholeDetectedResponse.getTimeDetected().toString(), potholeDetectedResponse.getAddress());
                            potholeModels.add(potholeModel);
                            Log.e("POThOLE MODEL:", potholeDetectedResponse.getAddress() + " " + potholeDetectedResponse.getTimeDetected());
                        }
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        adapter = new PotholeItemAdapter(potholeModels, getContext());
                        recyclerView.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<PotholeDetectedResponse>> call, Throwable throwable) {

            }
        });
    }
}