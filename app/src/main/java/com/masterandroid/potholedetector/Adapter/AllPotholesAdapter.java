package com.masterandroid.potholedetector.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.masterandroid.potholedetector.API.DTO.Response.PotholePotentialResponse;
import com.masterandroid.potholedetector.Event.OperatorPotholeInterface;
import com.masterandroid.potholedetector.R;

import java.util.List;

public class AllPotholesAdapter extends RecyclerView.Adapter<AllPotholesAdapter.ViewHolder> {

    private List<PotholePotentialResponse> potholePotentials;
    private Context context;
    private OperatorPotholeInterface operatorPotholeInterface;

    public AllPotholesAdapter(List<PotholePotentialResponse> potholePotentials, Context context, OperatorPotholeInterface operatorPotholeInterface) {
        this.potholePotentials = potholePotentials;
        this.context = context;
        this.operatorPotholeInterface = operatorPotholeInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.notification_item, parent, false);

        return new AllPotholesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvAddress.setText(potholePotentials.get(position).getAddress());
        holder.tvDateTime.setText(potholePotentials.get(position).getTimeDetected().toString());
    }

    @Override
    public int getItemCount() {
        return potholePotentials.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvAddress, tvDateTime;
        Button btnAccept, btnDeny;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAddress = itemView.findViewById(R.id.notiAddress);
            tvDateTime = itemView.findViewById(R.id.notiTimeDetect);
            btnAccept = itemView.findViewById(R.id.notiAccept);
            btnDeny = itemView.findViewById(R.id.notiDeny);

            btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (operatorPotholeInterface != null) {
                        int position = getLayoutPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            operatorPotholeInterface.onAccept(position);
                        }
                    }
                }
            });

            btnDeny.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (operatorPotholeInterface != null) {
                        int position = getLayoutPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            operatorPotholeInterface.onDeny(position);
                        }
                    }
                }
            });
        }
    }
}
