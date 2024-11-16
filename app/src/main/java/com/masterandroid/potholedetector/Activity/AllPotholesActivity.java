package com.masterandroid.potholedetector.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.masterandroid.potholedetector.Adapter.AllPotholesAdapter;
import com.masterandroid.potholedetector.Model.PotholeModel;
import com.masterandroid.potholedetector.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AllPotholesActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private AllPotholesAdapter adapter;
    private Map<String,ArrayList<PotholeModel>> groupedItems;
    private Toolbar toolbar;

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
        initData();
        setupRecyclerView();

        toolbar = findViewById(R.id.allPotholeToolBar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initData() {
        groupedItems = new HashMap<>();
        float date = 24;

        for (int i = 0; i < 16; i++) {
            date -= 0.5;

            int roundedDate = Math.round(date);
            String dateString = roundedDate + "/10/2024";

            PotholeModel pothole = new PotholeModel("Detect", dateString + ", 4:14 PM",
                    "Tạ Quang Bửu, Khu phố 6, phường Linh Trung, thành phố Thủ Đức, TP. HCM");

            if (!groupedItems.containsKey(dateString)) {
                groupedItems.put(dateString, new ArrayList<>());
            }
            groupedItems.get(dateString).add(pothole);
        }
    }


    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.allPotholeRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new AllPotholesAdapter(groupedItems);
        recyclerView.setAdapter(adapter);
    }

}