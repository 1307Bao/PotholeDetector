package com.masterandroid.potholedetector.Frontend.Service;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.masterandroid.potholedetector.Frontend.API.ApiClient;
import com.masterandroid.potholedetector.Frontend.API.ApiService;
import com.masterandroid.potholedetector.Frontend.API.DTO.Request.ApiResponse;
import com.masterandroid.potholedetector.Frontend.Security.SecureStorage;
import com.masterandroid.potholedetector.R;

import java.io.IOException;
import java.security.GeneralSecurityException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PotholeDetectionService extends Service implements SensorEventListener {
    // Constants
    private static final String CHANNEL_ID = "PotholeDetectionChannel";
    private static final int WINDOW_SIZE = 5;
    private static final String TOKEN_FLAG = "TOKEN_FLAG";
    private static final long COLLECTION_INTERVAL = 3000; // 3 seconds in milliseconds
    private static final float ALPHA = 0.8f; // Low-pass filter coefficient

    // Service components
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private ApiService apiService;
    private String token;
    private FusedLocationProviderClient locationClient;
    private LocationCallback locationCallback;

    // Data processing fields
    private final float[] movingAverageWindow = new float[WINDOW_SIZE];
    private int windowIndex = 0;
    private float[] gravity = new float[3];
    private float maxAcceleration = 0f;
    private long lastRequestTime = 0;
    private Location lastLocation = null;
    private float THRESHOLD;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeComponents();
        setupLocationUpdates();
    }

    private void initializeComponents() {
        // Initialize API service
        try {
            SecureStorage secureStorage = new SecureStorage(this);
            token = secureStorage.getValue(TOKEN_FLAG);
            apiService = ApiClient.getClientWithToken(token).create(ApiService.class);
            SharedPreferences sharedPreferences = getSharedPreferences("Sensor", MODE_PRIVATE);
            THRESHOLD = sharedPreferences.getInt("sensor", 10);
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }

        // Create notification channel and start foreground service
        createNotificationChannel();
        startForeground(2, getNotification());

        // Initialize sensors
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        locationClient = LocationServices.getFusedLocationProviderClient(this);

        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        }
    }

    private void setupLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(1000); // Update location every second

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    lastLocation = locationResult.getLastLocation();
                }
            }
        };

        locationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cleanup();
    }

    private void cleanup() {
        stopForeground(true);
        sensorManager.unregisterListener(this);
        if (locationCallback != null) {
            locationClient.removeLocationUpdates(locationCallback);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER) {
            return;
        }

        float[] filteredValues = applyLowPassFilter(event.values);
        float totalAcceleration = calculateTotalAcceleration(filteredValues);
        float filteredAcceleration = applyMovingAverageFilter(totalAcceleration);

        processAccelerationData(filteredAcceleration);
    }

    private float calculateTotalAcceleration(float[] values) {
        return (float) Math.sqrt(values[0] * values[0] +
                values[1] * values[1] +
                values[2] * values[2]);
    }

    private void processAccelerationData(float filteredAcceleration) {
        maxAcceleration = Math.max(maxAcceleration, filteredAcceleration);

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastRequestTime >= COLLECTION_INTERVAL) {
            checkAndSendPotholeData();
            lastRequestTime = currentTime;
            maxAcceleration = 0f;
        }
    }

    private void checkAndSendPotholeData() {
        if (maxAcceleration > THRESHOLD && lastLocation != null) {
            sendPotholeRequest(lastLocation.getLatitude(), lastLocation.getLongitude());
        }
    }

    private float applyMovingAverageFilter(float totalAcceleration) {
        movingAverageWindow[windowIndex] = totalAcceleration;
        windowIndex = (windowIndex + 1) % WINDOW_SIZE;

        float sum = 0;
        for (float value : movingAverageWindow) {
            sum += value;
        }
        return sum / WINDOW_SIZE;
    }

    private float[] applyLowPassFilter(float[] input) {
        gravity[0] = ALPHA * gravity[0] + (1 - ALPHA) * input[0];
        gravity[1] = ALPHA * gravity[1] + (1 - ALPHA) * input[1];
        gravity[2] = ALPHA * gravity[2] + (1 - ALPHA) * input[2];

        return new float[] {
                input[0] - gravity[0],
                input[1] - gravity[1],
                input[2] - gravity[2]
        };
    }

    private void sendPotholeRequest(double latitude, double longitude) {
        Call<ApiResponse<Void>> createPothole = apiService.addMorePotentialPothole(longitude, latitude);
        createPothole.enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful()) {
                    Log.d("PotholeDetection", "Pothole created at: " + latitude + ", " + longitude);
                } else {
                    Log.e("PotholeDetection", "Failed to create pothole: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                Log.e("PotholeDetection", "Network error while creating pothole", t);
            }
        });
    }

    private Notification getNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Pothole Detection Active")
                .setContentText("Monitoring road conditions...")
                .setSmallIcon(R.drawable.baseline_notifications_24)
                .setPriority(NotificationCompat.PRIORITY_LOW);

        return builder.build();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Pothole Detection Service Channel",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel.setDescription("Used for the pothole detection service");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used in this implementation
    }
}