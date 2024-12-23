package com.masterandroid.potholedetector.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.gestures.MoveGestureDetector;
import static com.mapbox.maps.plugin.animation.CameraAnimationsUtils.getCamera;
import static com.mapbox.maps.plugin.gestures.GesturesUtils.addOnMapClickListener;
import static com.mapbox.maps.plugin.gestures.GesturesUtils.getGestures;
import static com.mapbox.maps.plugin.locationcomponent.LocationComponentUtils.getLocationComponent;
import static com.mapbox.navigation.base.extensions.RouteOptionsExtensions.applyDefaultNavigationOptions;


import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.models.Bearing;
import com.mapbox.api.directions.v5.models.RouteOptions;
import com.mapbox.bindgen.Expected;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.utils.PolylineUtils;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.EdgeInsets;
import com.mapbox.maps.MapView;
import com.mapbox.maps.Style;
import com.mapbox.maps.extension.style.layers.properties.generated.TextAnchor;
import com.mapbox.maps.plugin.animation.MapAnimationOptions;
import com.mapbox.maps.plugin.annotation.AnnotationPlugin;
import com.mapbox.maps.plugin.annotation.AnnotationPluginImplKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManagerKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions;
import com.mapbox.maps.plugin.gestures.OnMapClickListener;
import com.mapbox.maps.plugin.gestures.OnMoveListener;
import com.mapbox.maps.plugin.locationcomponent.LocationComponentConstants;
import com.mapbox.maps.plugin.locationcomponent.LocationComponentPlugin;
import com.mapbox.maps.plugin.locationcomponent.generated.LocationComponentSettings;
import com.mapbox.navigation.base.options.NavigationOptions;
import com.mapbox.navigation.base.route.NavigationRoute;
import com.mapbox.navigation.base.route.NavigationRouterCallback;
import com.mapbox.navigation.base.route.RouterFailure;
import com.mapbox.navigation.base.route.RouterOrigin;
import com.mapbox.navigation.core.MapboxNavigation;
import com.mapbox.navigation.core.directions.session.RoutesObserver;
import com.mapbox.navigation.core.directions.session.RoutesUpdatedResult;
import com.mapbox.navigation.core.lifecycle.MapboxNavigationApp;
import com.mapbox.navigation.core.trip.session.LocationMatcherResult;
import com.mapbox.navigation.core.trip.session.LocationObserver;
import com.mapbox.navigation.ui.base.util.MapboxNavigationConsumer;
import com.mapbox.navigation.ui.maps.location.NavigationLocationProvider;
import com.mapbox.navigation.ui.maps.route.line.api.MapboxRouteLineApi;
import com.mapbox.navigation.ui.maps.route.line.api.MapboxRouteLineView;
import com.mapbox.navigation.ui.maps.route.line.model.MapboxRouteLineOptions;
import com.mapbox.navigation.ui.maps.route.line.model.RouteLineClearValue;
import com.mapbox.navigation.ui.maps.route.line.model.RouteLineColorResources;
import com.mapbox.navigation.ui.maps.route.line.model.RouteLineError;
import com.mapbox.navigation.ui.maps.route.line.model.RouteLineResources;
import com.mapbox.navigation.ui.maps.route.line.model.RouteSetValue;
import com.masterandroid.potholedetector.API.ApiClient;
import com.masterandroid.potholedetector.API.ApiService;
import com.masterandroid.potholedetector.API.DTO.Request.ApiResponse;
import com.masterandroid.potholedetector.API.DTO.Request.RouteRequest;
import com.masterandroid.potholedetector.API.DTO.Response.PotholeResponse;
import com.masterandroid.potholedetector.R;
import com.masterandroid.potholedetector.Security.SecureStorage;
import com.masterandroid.potholedetector.Service.LocationTrackingService;
import com.masterandroid.potholedetector.Service.PotholeDetectionService;

import java.io.IOException;
import java.io.Serializable;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
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

    private MapView mapView;
    private FloatingActionButton focusLocationBtn;
    private MaterialButton setRoute;

    private NavigationLocationProvider navigationLocationProvider = new NavigationLocationProvider();
    private MapboxRouteLineApi routeLineApi;
    private MapboxRouteLineView routeLineView;

    private CardView cardView;
    private Button btnCancel;
    private TextView informationPothole;
    private TextInputEditText editText;

    private Intent serviceIntent;
    private Intent locationTrackingIntent;

    private boolean isHide = false;
    private boolean isNavigate = false;
    private long currentRouteRequestId = -1;
    private List<PotholeResponse> listPothole;
    private ApiService apiService;
    private PointAnnotationManager pointAnnotationManager;

    private final LocationObserver locationObserver = new LocationObserver() {
        @Override
        public void onNewRawLocation(@NonNull android.location.Location location) {

        }

        @Override
        public void onNewLocationMatcherResult(@NonNull LocationMatcherResult locationMatcherResult) {
            Location location = locationMatcherResult.getEnhancedLocation();
            navigationLocationProvider.changePosition(location, locationMatcherResult.getKeyPoints(),null, null);
            if (focusLocation) {
                updateCamera(Point.fromLngLat(location.getLongitude(), location.getLatitude()), (double) location.getBearing());
            }
        }
    };

    private final RoutesObserver routesObserver = new RoutesObserver() {
        @Override
        public void onRoutesChanged(@NonNull RoutesUpdatedResult routesUpdatedResult) {
            routeLineApi.setNavigationRoutes(routesUpdatedResult.getNavigationRoutes(), new MapboxNavigationConsumer<Expected<RouteLineError, RouteSetValue>>() {
                @Override
                public void accept(Expected<RouteLineError, RouteSetValue> routeLineErrorRouteSetValueExpected) {
                    Style style = mapView.getMapboxMap().getStyle();
                    if (style != null) {
                        Log.e("COLOR", "ADD COLOR");
                        routeLineView.renderRouteDrawData(style, routeLineErrorRouteSetValueExpected);
                    }
                }
            });

        }
    };

    private boolean focusLocation = true;
    private MapboxNavigation mapboxNavigation;

    private void updateCamera(Point point, Double bearing) {
        MapAnimationOptions mapAnimationOptions = new MapAnimationOptions.Builder().duration(1500L).build();
        CameraOptions cameraOptions = new CameraOptions.Builder().center(point).zoom(18.0).bearing(bearing).pitch(45.00)
                .padding(new EdgeInsets(1000.0, 0.0, 0.0, 0.0)).build();

        getCamera(mapView).easeTo(cameraOptions, mapAnimationOptions);
    };

    private final OnMoveListener onMoveListener = new OnMoveListener() {
        @Override
        public void onMoveBegin(@NonNull MoveGestureDetector moveGestureDetector) {
            focusLocation = false;
            getGestures(mapView).removeOnMoveListener(onMoveListener);
            focusLocationBtn.show();
        }

        @Override
        public boolean onMove(@NonNull MoveGestureDetector moveGestureDetector) {
            return false;
        }

        @Override
        public void onMoveEnd(@NonNull MoveGestureDetector moveGestureDetector) {

        }
    };

    private final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean result) {
            if (!result) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        .setData(Uri.fromParts("package", requireContext().getPackageName(), null));
                startActivity(intent);
                requireActivity().finish();
            }
        }
    });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        try {
            SecureStorage secureStorage = new SecureStorage(requireContext());
            String token = secureStorage.getToken("TOKEN_FLAG");
            Log.e("TOKEN IN FRAGMENT:", token);
            apiService = ApiClient.getClientWithToken(token).create(ApiService.class);
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }

        serviceIntent = new Intent(requireContext(), PotholeDetectionService.class);
        locationTrackingIntent = new Intent(requireContext(), LocationTrackingService.class);

        mapView = view.findViewById(R.id.mapView);
        focusLocationBtn = view.findViewById(R.id.focusLocation);
        setRoute = view.findViewById(R.id.setRoute);
        editText = view.findViewById(R.id.edit_query);

        cardView = view.findViewById(R.id.functionalContainer);
        btnCancel = view.findViewById(R.id.disable);
        informationPothole = view.findViewById(R.id.informationPotholes);

        cardView.setVisibility(View.GONE);
        btnCancel.setVisibility(View.GONE);

        AnnotationPlugin annotationPlugin = AnnotationPluginImplKt.getAnnotations(mapView);
        pointAnnotationManager = PointAnnotationManagerKt.createPointAnnotationManager(annotationPlugin, mapView);

        try {
            initData();
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }

        setUpFullComponent();
        handleCancel();
        handleSearch();
        checkAndRequestPermission(); 
        handleEventOnMap();

        return view;
    }

    private void setUpFullComponent() {
        RouteLineColorResources colorResources = new RouteLineColorResources.Builder()
                .routeDefaultColor(Color.parseColor("#3BB2D0"))  // Màu mặc định cho đường đi
                .routeCasingColor(Color.parseColor("#2D7A9E"))  // Màu viền của đường đi
                .build();

        RouteLineResources lineResources = new RouteLineResources.Builder()
                .routeLineColorResources(colorResources)
                .build();


        MapboxRouteLineOptions options = new MapboxRouteLineOptions.Builder(requireContext())
                .withRouteLineResources(lineResources)
                .withRouteLineBelowLayerId(LocationComponentConstants.LOCATION_INDICATOR_LAYER)
                .build();
        routeLineView = new MapboxRouteLineView(options);
        routeLineApi = new MapboxRouteLineApi(options);


        NavigationOptions navigationOptions = new NavigationOptions.Builder(requireContext()).accessToken(getString(R.string.mapbox_access_token)).build();
        MapboxNavigationApp.setup(navigationOptions);

        mapboxNavigation = new MapboxNavigation(navigationOptions);
        mapboxNavigation.registerRoutesObserver(routesObserver);
        mapboxNavigation.registerLocationObserver(locationObserver);

        focusLocationBtn.hide();
        getGestures(mapView).addOnMoveListener(onMoveListener);
    }

    private void handleCancel() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentRouteRequestId != -1) {
                    mapboxNavigation.cancelRouteRequest(currentRouteRequestId);
                    currentRouteRequestId = -1;
                    setRoute.setVisibility(View.VISIBLE);
                    btnCancel.setVisibility(View.GONE);
                    isNavigate = false;

                    routeLineApi.clearRouteLine(new MapboxNavigationConsumer<Expected<RouteLineError, RouteLineClearValue>>() {
                        @Override
                        public void accept(Expected<RouteLineError, RouteLineClearValue> routeLineErrorRouteLineClearValueExpected) {
                            Style style = mapView.getMapboxMap().getStyle();
                            if (style != null) {
                                routeLineView.renderClearRouteLineValue(style, routeLineErrorRouteLineClearValueExpected);
                            }
                        }
                    });

                    requireActivity().stopService(serviceIntent);
                    requireActivity().stopService(locationTrackingIntent);

                    RelativeLayout.LayoutParams layoutParams =
                            (RelativeLayout.LayoutParams) informationPothole.getLayoutParams();
                    layoutParams.addRule(RelativeLayout.END_OF, R.id.setRoute);
                    informationPothole.setLayoutParams(layoutParams);

                    mapboxNavigation.stopTripSession();
                }
            }
        });
    }

    private void handleSearch() {
        editText.setSingleLine(true);

        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
                    String query = Objects.requireNonNull(editText.getText()).toString();
                    Geocoder geo = new Geocoder(requireContext());
                    try {
                        List<Address> addresses = geo.getFromLocationName(query, 1);
                        assert addresses != null;
                        Log.e("ADDRESS", "POINT: " + addresses.size());

                        if (!addresses.isEmpty()) {
                            Address address = addresses.get(0);
                            focusLocation = false;
                            focusLocationBtn.show();
                            Point point = Point.fromLngLat(address.getLongitude(), address.getLatitude());

                            updateCamera(point, 0.00);

                            Bitmap bitmap = BitmapFactory.decodeResource(requireActivity().getResources(), R.drawable.location_pin);

                            pointAnnotationManager.deleteAll();
                            PointAnnotationOptions pointAnnotationOptions = new PointAnnotationOptions().withTextAnchor(TextAnchor.CENTER).withIconImage(bitmap)
                                    .withPoint(point);
                            pointAnnotationManager.create(pointAnnotationOptions);

                            cardView.setVisibility(View.VISIBLE);

                            setRoute.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    fetchRoute(point);
                                }
                            });

                            focusLocationBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    focusLocation = true;
                                    getGestures(mapView).addOnMoveListener(onMoveListener);
                                    focusLocationBtn.hide();
                                }
                            });
                            return true;
                        }

                        return true;
                    } catch (IOException e) {
                        Toast.makeText(requireContext(), "Try Again", Toast.LENGTH_SHORT).show();
                        throw new RuntimeException(e);
                    }
                }
                return false;
            }
        });
    }

    private void handleEventOnMap() {
        LocationComponentPlugin locationComponentPlugin = getLocationComponent(mapView);
        setRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Please select a location in map", Toast.LENGTH_SHORT).show();
            }
        });

        mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                mapView.getMapboxMap().setCamera(new CameraOptions.Builder().zoom(20.0).build());

                locationComponentPlugin.setEnabled(true);
                locationComponentPlugin.setLocationProvider(navigationLocationProvider);
                getGestures(mapView).addOnMoveListener(onMoveListener);

                locationComponentPlugin.updateSettings(new Function1<LocationComponentSettings, Unit>() {
                    @Override
                    public Unit invoke(LocationComponentSettings locationComponentSettings) {
                        locationComponentSettings.setPulsingEnabled(true);
                        locationComponentSettings.setEnabled(true);
                        return null;
                    }
                });

                Bitmap bitmap = BitmapFactory.decodeResource(requireActivity().getResources(), R.drawable.location_pin);
                addOnMapClickListener(mapView.getMapboxMap(), new OnMapClickListener() {
                    @Override
                    public boolean onMapClick(@NonNull Point point) {
                        if (!isNavigate) {
                            pointAnnotationManager.deleteAll();
                            PointAnnotationOptions pointAnnotationOptions = new PointAnnotationOptions().withTextAnchor(TextAnchor.CENTER).withIconImage(bitmap)
                                    .withPoint(point);
                            cardView.setVisibility(View.VISIBLE);
                            pointAnnotationManager.create(pointAnnotationOptions);

                            setRoute.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    fetchRoute(point);
                                }
                            });
                        } else {
                            isHide = !isHide;
                            if (isHide) {
                                cardView.setVisibility(View.GONE);
                                editText.setVisibility(View.GONE);
                                return true;
                            }

                            cardView.setVisibility(View.VISIBLE);
                            editText.setVisibility(View.VISIBLE);
                        }
                        return true;
                    }
                });

                focusLocationBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        focusLocation = true;
                        getGestures(mapView).addOnMoveListener(onMoveListener);
                        focusLocationBtn.hide();
                    }
                });
            }
        });
    }

    private void checkAndRequestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                activityResultLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        || ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            activityResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            activityResultLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION);
        } else {
            mapboxNavigation.startTripSession();
        }
    }

    @SuppressLint("MissingPermission")
    private void fetchRoute(Point point) {
        LocationEngine locationEngine = LocationEngineProvider.getBestLocationEngine(requireContext());
        locationEngine.getLastLocation(new LocationEngineCallback<LocationEngineResult>() {
            @Override
            public void onSuccess(LocationEngineResult result) {
                Location location = result.getLastLocation();
                setRoute.setEnabled(false);
                RouteOptions.Builder builder = RouteOptions.builder();
                Point origin = Point.fromLngLat(Objects.requireNonNull(location).getLongitude(), location.getLatitude());
                builder.coordinatesList(Arrays.asList(origin, point));
                builder.alternatives(false);
                builder.profile(DirectionsCriteria.PROFILE_DRIVING);
                builder.bearingsList(Arrays.asList(Bearing.builder().angle(location.getBearing()).degrees(45.0).build(), null));
                applyDefaultNavigationOptions(builder);

                currentRouteRequestId = mapboxNavigation.requestRoutes(builder.build(), new NavigationRouterCallback() {
                    @Override
                    public void onRoutesReady(@NonNull List<NavigationRoute> list, @NonNull RouterOrigin routerOrigin) {
                        NavigationRoute route = list.get(0);
                        String geometry = route.getDirectionsRoute().geometry();

                        if (geometry != null) {
                            List<RouteRequest> routeRequestList = new ArrayList<>();
                            List<Point> points = PolylineUtils.decode(geometry, 6);
                            for (Point point : points) {
                                routeRequestList.add(new RouteRequest(point.longitude(), point.latitude()));
                            }
                            handleAlertPothole(points);
                        }


                        mapboxNavigation.setNavigationRoutes(list);
                        focusLocationBtn.performClick();
                        setRoute.setEnabled(true);

                        setOnClickSetRoute();

                        mapboxNavigation.startTripSession();
                    }

                    @Override
                    public void onFailure(@NonNull List<RouterFailure> list, @NonNull RouteOptions routeOptions) {
                        setRoute.setEnabled(true);

                        Toast.makeText(requireContext(), "Route request failed", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCanceled(@NonNull RouteOptions routeOptions, @NonNull RouterOrigin routerOrigin) {

                    }
                });
            }

            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });
    }

    private void setOnClickSetRoute() {
        isNavigate = true;
        setRoute.setVisibility(View.GONE);
        btnCancel.setVisibility(View.VISIBLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            requireActivity().startForegroundService(serviceIntent);
        } else {
            requireActivity().startService(serviceIntent);
        }

        RelativeLayout.LayoutParams layoutParams =
                (RelativeLayout.LayoutParams) informationPothole.getLayoutParams();
        layoutParams.addRule(RelativeLayout.END_OF, R.id.disable);
        informationPothole.setLayoutParams(layoutParams);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapboxNavigation.onDestroy();
        mapboxNavigation.unregisterRoutesObserver(routesObserver);
        mapboxNavigation.unregisterLocationObserver(locationObserver);
    }

    public void initData() throws GeneralSecurityException, IOException {

        Call<ApiResponse<List<PotholeResponse>>> getAllPotholes = apiService.getPotholes();
        getAllPotholes.enqueue(new Callback<ApiResponse<List<PotholeResponse>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<PotholeResponse>>> call, @NonNull Response<ApiResponse<List<PotholeResponse>>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    listPothole = response.body().getResult();

                    drawPothole();
                    Log.e("GET POTHOLE", listPothole.size() + "");

                } else {
                    Toast.makeText(requireContext(), response.code() + "", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<PotholeResponse>>> call, @NonNull Throwable throwable) {
                Toast.makeText(requireContext(), "GET POTHOLE FAILED 2", Toast.LENGTH_SHORT).show();
                Log.e("ERROR GET POTHOLE", throwable.getMessage());
            }
        });
    }

    public void drawPothole() {
        if (listPothole != null) {
            Log.e("DRAW", listPothole.size() + "");
            Bitmap bitmap = BitmapFactory.decodeResource(requireActivity().getResources(), R.drawable.pothole);
            AnnotationPlugin annotationPlugin = AnnotationPluginImplKt.getAnnotations(mapView);
            PointAnnotationManager pointAnnotation = PointAnnotationManagerKt.createPointAnnotationManager(annotationPlugin, mapView);

            for (PotholeResponse potholeResponse : listPothole) {
                Log.e("DRAW", potholeResponse.getLongitude()+" "+potholeResponse.getLatitude());
                Point point = Point.fromLngLat(potholeResponse.getLongitude(), potholeResponse.getLatitude());
                PointAnnotationOptions pointAnnotationOptions = new PointAnnotationOptions().withTextAnchor(TextAnchor.CENTER).withIconImage(bitmap)
                        .withPoint(point);
                pointAnnotation.create(pointAnnotationOptions);
            }
        }
    }

    private void handleAlertPothole(List<Point> waypoints) {
        List<RouteRequest> requests = new ArrayList<>();
        for (Point waypoint : waypoints) {
            requests.add(new RouteRequest(waypoint.longitude(), waypoint.latitude()));
        }

        Call<ApiResponse<List<PotholeResponse>>> getPotholeOnMap = apiService.getAllPotholeOnMap(requests);
        getPotholeOnMap.enqueue(new Callback<ApiResponse<List<PotholeResponse>>>(){

            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<PotholeResponse>>> call, @NonNull Response<ApiResponse<List<PotholeResponse>>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    listPothole = response.body().getResult();
                    informationPothole.setText("Number of Potholes: " + listPothole.size() + " potholes");

                    for (PotholeResponse potholeResponse : listPothole) {
                        Log.e("DRAW", potholeResponse.getLongitude()+" "+potholeResponse.getLatitude());
                    }

                    locationTrackingIntent.putExtra("listPothole", (Serializable) listPothole);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        requireContext().startForegroundService(locationTrackingIntent);
                    } else {
                        requireContext().startService(locationTrackingIntent);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<PotholeResponse>>> call, @NonNull Throwable throwable) {

            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroy();
        if (mapView != null) {
            mapView.onDestroy(); // Giải phóng tài nguyên Mapbox
        }
    }


}