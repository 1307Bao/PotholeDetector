package com.masterandroid.potholedetector.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
//import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.masterandroid.potholedetector.Adapter.PotholeItemAdapter;
import com.masterandroid.potholedetector.Model.PotholeModel;
import com.masterandroid.potholedetector.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ReportFragment extends Fragment {

    private BarChart barChart;
    private RecyclerView recyclerView;
    private PotholeItemAdapter adapter;
    private Map<String, ArrayList<PotholeModel>> groupedPotholes;

    public ReportFragment() {
        // Required empty public constructor
    }

    public static ReportFragment newInstance() {
        return new ReportFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        // Khởi tạo BarChart
        barChart = view.findViewById(R.id.barChart);
        setupBarChart();

        // Khởi tạo RecyclerView
        recyclerView = view.findViewById(R.id.reportRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Khởi tạo dữ liệu và adapter
        initData();
        adapter = new PotholeItemAdapter(new ArrayList<>(getAllPotholes()), getContext());
        recyclerView.setAdapter(adapter);

        // Hiển thị tổng số pothole và khoảng thời gian
        TextView totalPotholesTextView = view.findViewById(R.id.totalPotholes);
        TextView dateRangeTextView = view.findViewById(R.id.dateRange);

        int totalPotholes = getAllPotholes().size();
        totalPotholesTextView.setText("Total potholes: " + totalPotholes);

        String dateRange = "November 3 - November 9"; // Hoặc tính toán dựa trên dữ liệu thực tế
        dateRangeTextView.setText(dateRange);
    }

    private void setupBarChart(){
        // Tạo dữ liệu cho biểu đồ
        List<BarEntry> entries = new ArrayList<>();
        // Giả sử dữ liệu số lượng pothole theo các ngày trong tuần từ 0 (Sunday) đến 6 (Saturday)
        entries.add(new BarEntry(0, 5));
        entries.add(new BarEntry(1, 3));
        entries.add(new BarEntry(2, 6));
        entries.add(new BarEntry(3, 2));
        entries.add(new BarEntry(4, 7));
        entries.add(new BarEntry(5, 4));
        entries.add(new BarEntry(6, 8));

        BarDataSet dataSet = new BarDataSet(entries, "Potholes per Day");
        int chartColor = getResources().getColor(R.color.light_primary_color); // Get the color from resources
        dataSet.setColor(chartColor); // Set the color of the bars


        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.6f); // Rộng của các cột

        barChart.setData(barData);
        barChart.setFitBars(true); // Làm cho các cột vừa với biểu đồ
        barChart.invalidate(); // Refresh biểu đồ

//        // Thiết lập mô tả cho biểu đồ
//        Description description = new Description();
//        description.setText("Số lượng pothole theo ngày trong tuần");
//        barChart.setDescription(description);

        // Thiết lập nhãn cho trục X
        String[] days = {"S", "M", "T", "W", "T", "F", "S"};
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(days));

        // Các thiết lập khác
        barChart.setDrawGridBackground(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setLabelCount(7);
        barChart.getXAxis().setPosition(com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM);
        barChart.getAxisRight().setEnabled(false);
    }

    private void initData(){
        groupedPotholes = new HashMap<>();
        // Giả sử dữ liệu từ thứ Hai đến Chủ Nhật
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

        for (String day : days) {
            ArrayList<PotholeModel> list = new ArrayList<>();
            // Giả sử mỗi ngày có từ 1 đến 5 pothole
            int count = (int) (Math.random() * 5) + 1;
            for (int i = 0; i < count; i++) {
                list.add(new PotholeModel("Detect", "24/10/2024, 4:14 PM",
                        "Địa chỉ pothole ở " + day + ", TP. HCM"));
            }
            groupedPotholes.put(day, list);
        }
    }

    private List<PotholeModel> getAllPotholes(){
        List<PotholeModel> allPotholes = new ArrayList<>();
        for (Map.Entry<String, ArrayList<PotholeModel>> entry : groupedPotholes.entrySet()){
            allPotholes.addAll(entry.getValue());
        }
        return allPotholes;
    }
}
