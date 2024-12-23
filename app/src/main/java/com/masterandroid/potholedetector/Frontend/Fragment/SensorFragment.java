package com.masterandroid.potholedetector.Frontend.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.masterandroid.potholedetector.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SensorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SensorFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SensorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SensorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SensorFragment newInstance(String param1, String param2) {
        SensorFragment fragment = new SensorFragment();
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

    private RadioGroup radioGroup;
    private RadioButton btnHigh, btnMedium, btnLow;
    private Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sensor, container, false);

        toolbar = view.findViewById(R.id.sensorToolbar);
        radioGroup = view.findViewById(R.id.sensorRadioGroup);
        btnHigh = view.findViewById(R.id.sensorRadioButtonHigh);
        btnMedium = view.findViewById(R.id.sensorRadioButtonMedium);
        btnLow = view.findViewById(R.id.sensorRadioButtonLow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.remove(SensorFragment.this);
                Fragment fragment = fragmentManager.findFragmentByTag("PROFILE");
                assert fragment != null;
                fragmentTransaction.show(fragment);
                fragmentTransaction.commit();
            }
        });

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("Sensor", Context.MODE_PRIVATE);
        int sensor = sharedPreferences.getInt("sensor", 10);

        if (sensor == 10) {
            btnHigh.setChecked(true);
        } else if (sensor == 12) {
            btnMedium.setChecked(true);
        } else {
            btnLow.setChecked(true);
        }

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.sensorRadioButtonHigh) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("sensor", 10);
                editor.apply();
            } else if (checkedId == R.id.sensorRadioButtonMedium) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("sensor", 12);
                editor.apply();
            } else {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("sensor", 15);
                editor.apply();
            }
        });

        return view;
    }
}