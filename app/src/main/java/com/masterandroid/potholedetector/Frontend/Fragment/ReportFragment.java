package com.masterandroid.potholedetector.Frontend.Fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
import com.masterandroid.potholedetector.Frontend.API.ApiClient;
import com.masterandroid.potholedetector.Frontend.API.ApiService;
import com.masterandroid.potholedetector.Frontend.API.DTO.Response.PotholeDetectedResponse;
import com.masterandroid.potholedetector.Frontend.API.DTO.Response.ReportPerDayResponse;
import com.masterandroid.potholedetector.Frontend.API.DTO.Response.ReportResponse;
import com.masterandroid.potholedetector.Frontend.Adapter.PotholeItemAdapter;
import com.masterandroid.potholedetector.Frontend.Model.PotholeModel;
import com.masterandroid.potholedetector.Frontend.Security.SecureStorage;
import com.masterandroid.potholedetector.R;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ReportFragment extends Fragment {

    private BarChart barChart;
    private RecyclerView recyclerView;
    private PotholeItemAdapter adapter;
    private ApiService apiService;
    private TextView tvDetected, tvEncountered, tvTotal;

    public ReportFragment() {
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
        tvDetected = view.findViewById(R.id.tvPotholeDetectedValue);
        tvEncountered = view.findViewById(R.id.tvPotholeEncounteredValue);
        tvTotal = view.findViewById(R.id.tvPotholeTotalValue);

        // Khởi tạo RecyclerView
        recyclerView = view.findViewById(R.id.reportRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Khởi tạo dữ liệu và adapter
        try {
            initData();
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }

        // Hiển thị tổng số pothole và khoảng thời gian
        TextView dateRangeTextView = view.findViewById(R.id.dateRange);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            initDateRange(dateRangeTextView);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initDateRange(TextView dateRangeTextView) {
        // Lấy ngày hiện tại
        LocalDate today = LocalDate.now();

        // Xác định ngày đầu tuần (Monday) và cuối tuần (Sunday)
        LocalDate startOfWeek = today.with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1); // Monday
        LocalDate endOfWeek = today.with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 7); // Sunday

        // Định dạng ngày và tháng
        DateTimeFormatter monthDayFormatter = DateTimeFormatter.ofPattern("MMMM d", Locale.ENGLISH);

        // Format ngày đầu tuần và cuối tuần
        String formattedStart = startOfWeek.format(monthDayFormatter);
        String formattedEnd = endOfWeek.format(monthDayFormatter);

        // Kết quả cuối cùng
        String currentWeek = formattedStart + " - " + formattedEnd;

        dateRangeTextView.setText(currentWeek);
    }

    private void setupBarChart(List<ReportPerDayResponse> report){
        // Tạo dữ liệu cho biểu đồ
        List<BarEntry> entries = new ArrayList<>();
        // Giả sử dữ liệu số lượng pothole theo các ngày trong tuần từ 0 (Sunday) đến 6 (Saturday)
        int numberOfDetected = 0;
        int numberOfEncountered = 0;
        int numberOfTotal = 0;

        for (int i = 0; i < 7; i++) {
            entries.add(new BarEntry(i, report.get(i).getTotal()));
            numberOfDetected += report.get(i).getTotalPotholeDetected();
            numberOfEncountered += report.get(i).getTotalPotholeEncountered();
            numberOfTotal += report.get(i).getTotal();
        }

        tvDetected.setText(String.valueOf(numberOfDetected));
        tvEncountered.setText(String.valueOf(numberOfEncountered));
        tvTotal.setText(String.valueOf(numberOfTotal));

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

    private void initData() throws GeneralSecurityException, IOException {
        SecureStorage secureStorage = new SecureStorage(requireContext());
        String token = secureStorage.getToken("TOKEN_FLAG");
        apiService = ApiClient.getClientWithToken(token).create(ApiService.class);

        Call<ReportResponse> call = apiService.getReport();
        call.enqueue(new Callback<ReportResponse>() {
            @Override
            public void onResponse(Call<ReportResponse> call, Response<ReportResponse> response) {
                if (response.isSuccessful()) {
                    ReportResponse reportResponse = response.body();
                    if (reportResponse != null) {
                        List<PotholeDetectedResponse> detectedResponses = reportResponse.getPotholeDetectedResponses();
                        List<ReportPerDayResponse> reportPerDayResponses = reportResponse.getReportPerDayResponses();

                        setupBarChart(reportPerDayResponses);
                        setUpRecyclerView(detectedResponses);
                    }
                }
            }

            @Override
            public void onFailure(Call<ReportResponse> call, Throwable throwable) {

            }
        });
    }

    private void setUpRecyclerView(List<PotholeDetectedResponse> detectedResponses) {
        ArrayList<PotholeModel> potholeModels = new ArrayList<>();
        for (PotholeDetectedResponse detectedResponse : detectedResponses) {
            PotholeModel potholeModel = new PotholeModel("DETECT",
                    detectedResponse.getTimeDetected().toString(), detectedResponse.getAddress());
            potholeModels.add(potholeModel);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PotholeItemAdapter(potholeModels, getContext());
        recyclerView.setAdapter(adapter);
    }
}
