package com.example.medisynchealthcareapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LabTestAdapter extends RecyclerView.Adapter<LabTestAdapter.LabTestViewHolder> {

    private final Context context;
    private final String[] labTests;

    public LabTestAdapter(Context context, String[] labTests) {
        this.context = context;
        this.labTests = labTests;
    }

    @NonNull
    @Override
    public LabTestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lab_test, parent, false);
        return new LabTestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LabTestViewHolder holder, int position) {
        String testName = labTests[position];
        holder.textTestName.setText(testName);

        // Set click listener here because testName is known here
        holder.buttonSeePrice.setOnClickListener(v -> {
            Intent intent = new Intent(context, PriceDetailsActivity.class);
            intent.putExtra("TEST_NAME", testName);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return labTests.length;
    }

    static class LabTestViewHolder extends RecyclerView.ViewHolder {

        TextView textTestName;
        Button buttonSeePrice;

        public LabTestViewHolder(@NonNull View itemView) {
            super(itemView);
            textTestName = itemView.findViewById(R.id.textTestName);
            buttonSeePrice = itemView.findViewById(R.id.buttonSeePrice);
        }
    }
}
