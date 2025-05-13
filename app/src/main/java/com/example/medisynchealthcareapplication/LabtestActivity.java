package com.example.medisynchealthcareapplication;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.GridLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class LabtestActivity extends AppCompatActivity {

    Button bBack;
    GridLayout labTestGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labtest);

        bBack = findViewById(R.id.buttonDDBack);
        bBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LabtestActivity.this,DashboardActivity.class));
            }
        });

        labTestGrid = findViewById(R.id.labTestGrid);

        String[] labTests = {
                "CBC Test", "Dengue Test", "Ultrasonography", "Blood Sugar Test",
                "Thyroid Test", "Liver Function Test", "Kidney Function Test", "ECG",
                "MRI Scan", "CT Scan", "X-Ray", "Blood Group Test",
                "COVID-19 PCR", "Cholesterol Test", "Hemoglobin Test"
        };

        for (String test : labTests) {
            TextView tile = new TextView(this);
            tile.setText(test);
            tile.setPadding(24, 24, 24, 24);
            tile.setTextSize(16);
            tile.setTypeface(Typeface.DEFAULT_BOLD);
            tile.setTextColor(ContextCompat.getColor(this, R.color.deepPurple));

            tile.setGravity(Gravity.CENTER);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            params.setMargins(16, 16, 16, 16);
            tile.setLayoutParams(params);

            labTestGrid.addView(tile);
        }
    }
}
