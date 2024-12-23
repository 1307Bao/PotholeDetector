package com.masterandroid.potholedetector.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.masterandroid.potholedetector.API.ApiClient;
import com.masterandroid.potholedetector.API.ApiService;
import com.masterandroid.potholedetector.API.DTO.Request.ApiResponse;
import com.masterandroid.potholedetector.API.DTO.Request.PotholePotentialRequest;
import com.masterandroid.potholedetector.API.DTO.Response.PotholePotentialResponse;
import com.masterandroid.potholedetector.Adapter.AllPotholesAdapter;
import com.masterandroid.potholedetector.Event.OperatorPotholeInterface;
import com.masterandroid.potholedetector.Model.PotholeModel;
import com.masterandroid.potholedetector.R;
import com.masterandroid.potholedetector.Security.SecureStorage;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllPotholesActivity extends BaseActivity implements OperatorPotholeInterface {

    private RecyclerView recyclerView;
    private AllPotholesAdapter adapter;
    private List<PotholePotentialResponse> data;
    private Toolbar toolbar;
    private ApiService apiService;
    private String token;
    private static final String TOKEN_FLAG = "TOKEN_FLAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_all_potholes);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        try {
            initData();
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }

        toolbar = findViewById(R.id.allPotholeToolBar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initData() throws GeneralSecurityException, IOException {
        SecureStorage secureStorage = new SecureStorage(this);
        token = secureStorage.getToken(TOKEN_FLAG);
        apiService = ApiClient.getClientWithToken(token).create(ApiService.class);
        data = null;
        Call<List<PotholePotentialResponse>> call = apiService.getAllPotholePotential();
        call.enqueue(new Callback<List<PotholePotentialResponse>>() {

            @Override
            public void onResponse(Call<List<PotholePotentialResponse>> call, Response<List<PotholePotentialResponse>> response) {
                if (response.isSuccessful()) {
                    data = response.body();
                    setupRecyclerView();
                }
            }

            @Override
            public void onFailure(Call<List<PotholePotentialResponse>> call, Throwable throwable) {
                Toast.makeText(AllPotholesActivity.this, "Some Error Occured", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setupRecyclerView() {
        if (data != null) {
            recyclerView = findViewById(R.id.allPotholeRecyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new AllPotholesAdapter(data, this, this);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onAccept(int position) {
        Call<ApiResponse<Void>> call = apiService.createPothole(data.get(position).getId());
        call.enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                Toast.makeText(AllPotholesActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                data.remove(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, data.size());
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable throwable) {
                Toast.makeText(AllPotholesActivity.this, "Try Again", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onDeny(int position) {
        Call<ApiResponse<Void>> call = apiService.deletePothole(data.get(position).getId());
        call.enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                Toast.makeText(AllPotholesActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                data.remove(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, data.size());
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable throwable) {
                Toast.makeText(AllPotholesActivity.this, "Try Again", Toast.LENGTH_SHORT).show();

            }
        });
    }
}