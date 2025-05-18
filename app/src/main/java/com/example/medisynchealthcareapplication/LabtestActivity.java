package com.example.medisynchealthcareapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LabtestActivity extends AppCompatActivity {

    private Button bBack;
    private RecyclerView labTestRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labtest);

        bBack = findViewById(R.id.buttonDDBack);
        labTestRecyclerView = findViewById(R.id.labTestRecyclerView);

        bBack.setOnClickListener(v -> startActivity(new Intent(LabtestActivity.this, DashboardActivity.class)));

        String[] labTests = {
                "CBC Test", "Dengue Test", "Ultrasonography", "Blood Sugar Test",
                "Thyroid Test", "Liver Function Test", "Kidney Function Test", "ECG",
                "MRI Scan", "CT Scan", "X-Ray", "Blood Group Test",
                "COVID-19 PCR", "Cholesterol Test", "Hemoglobin Test"
        };

        // Set layout manager and adapter for RecyclerView
        labTestRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        labTestRecyclerView.setAdapter(new LabTestAdapter(this, labTests));
    }
}
