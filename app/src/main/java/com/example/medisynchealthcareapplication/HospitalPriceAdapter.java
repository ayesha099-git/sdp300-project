package com.example.medisynchealthcareapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class HospitalPriceAdapter extends RecyclerView.Adapter<HospitalPriceAdapter.ViewHolder> {

    private final Context context;
    private final List<HospitalPrice> hospitalPrices;
    private final OnHospitalClickListener listener;

    // Listener interface for clicks
    public interface OnHospitalClickListener {
        void onHospitalClick(String hospitalName, String price);
    }

    // Constructor
    public HospitalPriceAdapter(Context context, List<HospitalPrice> hospitalPrices, OnHospitalClickListener listener) {
        this.context = context;
        this.hospitalPrices = hospitalPrices;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_hospital_price, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HospitalPrice hospitalPrice = hospitalPrices.get(position);
        holder.textHospitalName.setText(hospitalPrice.getHospitalName());

        // Format price without decimals
        String priceStr = String.format(Locale.getDefault(), "%.0f", hospitalPrice.getPrice());
        holder.textHospitalPrice.setText("৳ " + priceStr);

        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            listener.onHospitalClick(hospitalPrice.getHospitalName(), priceStr);
        });
    }

    @Override
    public int getItemCount() {
        return hospitalPrices.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textHospitalName, textHospitalPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textHospitalName = itemView.findViewById(R.id.textHospitalName);
            textHospitalPrice = itemView.findViewById(R.id.textHospitalPrice);
        }
    }
}
