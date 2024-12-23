package com.masterandroid.potholedetector.Frontend.Service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mapbox.android.core.location.*;
import com.masterandroid.potholedetector.Frontend.API.ApiClient;
import com.masterandroid.potholedetector.Frontend.API.ApiService;
import com.masterandroid.potholedetector.Frontend.API.DTO.Point;
import com.masterandroid.potholedetector.Frontend.API.DTO.Request.PotholeRequest;
import com.masterandroid.potholedetector.Frontend.API.DTO.Response.PotholeResponse;
import com.masterandroid.potholedetector.Frontend.Security.SecureStorage;
import com.masterandroid.potholedetector.R;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationTrackingService extends Service {
    private static final String TAG = "LocationTrackingService";
    private static final String CHANNEL_ID = "PotholeChannel";
    private static final int NOTIFICATION_ID = 1;
    private static final double WARNING_DISTANCE = 50; // meters
    private static final double ENCOUNTER_DISTANCE = 10; // meters

    private LocationEngine locationEngine;
    private CopyOnWriteArrayList<PotholeResponse> listPothole; // Thread-safe list
    private ApiService apiService;
    private String token;
    private NotificationManager notificationManager;

    private final LocationEngineCallback<LocationEngineResult> callback = new LocationEngineCallback<LocationEngineResult>() {
        @Override
        public void onSuccess(LocationEngineResult result) {
            Location location = result.getLastLocation();
            if (location != null) {
                Point currentPoint = new Point(location.getLongitude(), location.getLatitude());
                checkProximityToPotholes(currentPoint);
            }
        }

        @Override
        public void onFailure(@NonNull Exception exception) {
            Log.e(TAG, "Failed to get location", exception);
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.hasExtra("listPothole")) {
            List<PotholeResponse> receivedList = (List<PotholeResponse>) intent.getSerializableExtra("listPothole");
            if (receivedList != null) {
                listPothole = new CopyOnWriteArrayList<>(receivedList);
            }
        }
        return START_STICKY; // Service will be restarted if killed
    }

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel();
        startForegroundService();
        initializeLocationEngine();
        initializeApiService();
    }

    private void createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Cảnh báo ổ gà",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Thông báo khi bạn đến gần ổ gà");
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void startForegroundService() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Notification notification = new Notification.Builder(this, CHANNEL_ID)
                    .setContentTitle("Dịch vụ theo dõi vị trí")
                    .setContentText("Đang chạy...")
                    .setSmallIcon(R.drawable.baseline_warning_amber_24)
                    .build();
            startForeground(NOTIFICATION_ID, notification);
        }
    }

    @SuppressLint("MissingPermission")
    private void initializeLocationEngine() {
        locationEngine = LocationEngineProvider.getBestLocationEngine(this);
        LocationEngineRequest request = new LocationEngineRequest.Builder(1000)
                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                .setFastestInterval(500)
                .build();

        try {
            locationEngine.requestLocationUpdates(request, callback, getMainLooper());
        } catch (Exception e) {
            Log.e(TAG, "Error requesting location updates", e);
        }
    }

    private void initializeApiService() {
        try {
            SecureStorage secureStorage = new SecureStorage(getApplicationContext());
            token = secureStorage.getValue("TOKEN_FLAG");
            if (token != null) {
                apiService = ApiClient.getClientWithToken(token).create(ApiService.class);
            }
        } catch (GeneralSecurityException | IOException e) {
            Log.e(TAG, "Error initializing API service", e);
        }
    }

    private void checkProximityToPotholes(Point currentLocation) {
        if (listPothole == null || listPothole.isEmpty()) return;

        for (PotholeResponse pothole : listPothole) {
            double distance = haversineDistance(currentLocation,
                    new Point(pothole.getLongitude(), pothole.getLatitude()));

            if (distance <= ENCOUNTER_DISTANCE) {
                handlePotholeEncounter(pothole);
            } else if (distance <= WARNING_DISTANCE) {
                showPotholeWarning();
            }
        }
    }

    private void handlePotholeEncounter(PotholeResponse pothole) {
        if (apiService == null) return;

        PotholeRequest request = new PotholeRequest(
                pothole.getId(),
                pothole.getLongitude(),
                pothole.getLatitude(),
                pothole.getAddress()
        );

        apiService.metPothole(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    listPothole.remove(pothole);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.e(TAG, "Failed to report pothole encounter", t);
            }
        });
    }

    private void showPotholeWarning() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Notification notification = new Notification.Builder(this, CHANNEL_ID)
                    .setContentTitle("Cảnh báo ổ gà")
                    .setContentText("Bạn sắp đến gần ổ gà!")
                    .setSmallIcon(R.drawable.baseline_warning_amber_24)
                    .build();
            notificationManager.notify(NOTIFICATION_ID, notification);
        }
    }

    private double haversineDistance(Point p1, Point p2) {
        final double R = 6371e3; // Earth's radius in meters
        double lat1 = Math.toRadians(p1.getLatitude());
        double lat2 = Math.toRadians(p2.getLatitude());
        double deltaLat = Math.toRadians(p2.getLatitude() - p1.getLatitude());
        double deltaLon = Math.toRadians(p2.getLongitude() - p1.getLongitude());

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        if (locationEngine != null) {
            locationEngine.removeLocationUpdates(callback);
        }
        super.onDestroy();
    }
}