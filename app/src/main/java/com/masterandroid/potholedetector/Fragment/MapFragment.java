package com.masterandroid.potholedetector.Fragment;

import android.content.pm.PackageManager;
import android.health.connect.datatypes.ExerciseRoute;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.Manifest;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.masterandroid.potholedetector.R;

import java.io.IOException;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private GoogleMap mMap;
    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;

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
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        getLastLocation();
        Toolbar toolbar = view.findViewById(R.id.mapToolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        // Inflate menu và thiết lập SearchView
        inflater.inflate(R.menu.map_search_toolbar, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        // Kiểm tra và ép kiểu SearchView
        if (searchItem != null) {
            SearchView searchView = (SearchView) searchItem.getActionView();
            if (searchView != null) {
                // Xử lý sự kiện tìm kiếm
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        // Xử lý khi người dùng nhấn nút "Search" trên bàn phím

                        String location = searchView.getQuery().toString();
                        List<Address> addressList = null;

                        if (location != null) {
                            Geocoder geocoder = new Geocoder(getActivity());

                            try {
                                addressList = geocoder.getFromLocationName(location, 1);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            Address address = addressList.get(0);
                            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                            mMap.addMarker(new MarkerOptions().position(latLng));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                        }

                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        // Xử lý khi nội dung tìm kiếm thay đổi
                        return true;
                    }
                });
            }
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            // Xử lý khi nhấn vào icon tìm kiếm, nếu cần
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        LatLng currentPosition = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(currentPosition));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 15));
    }

    private void enableMyLocation() {
        if (mMap != null) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true); // Bật hiển thị vị trí hiện tại của người dùng
        }
    }

    private void getLastLocation() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            enableMyLocation();
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;

                    SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                            .findFragmentById(R.id.mapGmap);
                    mapFragment.getMapAsync(MapFragment.this);
                }
            }
        });
    }
}