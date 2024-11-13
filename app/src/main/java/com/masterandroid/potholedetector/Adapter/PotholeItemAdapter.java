package com.masterandroid.potholedetector.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.masterandroid.potholedetector.Model.PotholeModel;
import com.masterandroid.potholedetector.R;

import java.util.ArrayList;

// Create adapter for recycler view in home fragment, to get recent pothole what driver faced

public class PotholeItemAdapter extends RecyclerView.Adapter<PotholeItemAdapter.ViewHolder> {

    private  ArrayList<PotholeModel> potholeModels;
    private Context context;

    public PotholeItemAdapter(ArrayList<PotholeModel> potholeModels, Context context) {
        this.potholeModels = potholeModels;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.porthole_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PotholeItemAdapter.ViewHolder holder, int position) {
        holder.tvStatus.setText(potholeModels.get(position).getStatus());
        holder.tvDateTime.setText(potholeModels.get(position).getDatetime());
        holder.tvAddress.setText(potholeModels.get(position).getAddress());
    }

    @Override
    public int getItemCount() {

        return potholeModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvStatus, tvAddress, tvDateTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvStatus = itemView.findViewById(R.id.status);
            tvDateTime = itemView.findViewById(R.id.dateTime);
            tvAddress = itemView.findViewById(R.id.address);
        }
    }
}
