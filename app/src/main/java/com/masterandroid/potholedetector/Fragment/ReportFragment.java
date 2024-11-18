package com.masterandroid.potholedetector.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
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
        TextView dateRangeTextView = view.findViewById(R.id.dateRange);
        TextView totalPotholesTextView = view.findViewById(R.id.totalPotholes);

        String dateRange = "3/11 - 9/11"; // Hoặc tính toán dựa trên dữ liệu thực tế
        dateRangeTextView.setText(dateRange);

        int totalPotholes = getAllPotholes().size();
        totalPotholesTextView.setText(getString(R.string.total_potholes_weekly) + ": " + totalPotholes);
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

        BarDataSet dataSet = new BarDataSet(entries, getString(R.string.potholes_per_day));

       // Get theme colors
        int chartBarColor = getThemeColor(requireContext(), R.attr.chartBarColor);
        int chartTextColor = getThemeColor(requireContext(), R.attr.chartTextColor);
        int chartBackgroundColor = getThemeColor(requireContext(), R.attr.chartBackgroundColor);
    
        // Apply colors
        dataSet.setColor(chartBarColor);
        dataSet.setValueTextColor(chartTextColor); // Set color for values above bars
        dataSet.setValueTextSize(12f); // Optional: adjust text size
        dataSet.setValueFormatter(new ValueFormatter() {
        @Override
        public String getFormattedValue(float value) {
            return String.valueOf((int) value); // Convert to integer
        }
    });
        barChart.setBackgroundColor(chartBackgroundColor);

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.6f); // Rộng của các cột

        barChart.setData(barData);
        barChart.setFitBars(true); // Làm cho các cột vừa với biểu đồ
        barChart.invalidate(); // Refresh biểu đồ

        // Ẩn description hoàn toàn
        Description description = new Description();
        description.setEnabled(false);  // Disable description
        barChart.setDescription(description);

        // Thiết lập nhãn cho trục X
        String[] days = {"S", "M", "T", "W", "T", "F", "S"};
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(days));

        // Set text colors
        barChart.getXAxis().setTextColor(chartTextColor);
        barChart.getAxisLeft().setTextColor(chartTextColor);
        barChart.getLegend().setTextColor(chartTextColor);

        // Các thiết lập khác
        barChart.setDrawGridBackground(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setLabelCount(7);
        barChart.getXAxis().setPosition(com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(days) {
            @Override
            public String getFormattedValue(float value) {
                return days[(int) value];
            }
        });
        barChart.getAxisRight().setEnabled(false);
    }

    // Add this helper method to get theme colors
    private int getThemeColor(Context context, int attr) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(attr, typedValue, true);
        return typedValue.data;
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
                list.add(new PotholeModel(getString(R.string.detect), "24/10/2024, 4:14 PM",
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
