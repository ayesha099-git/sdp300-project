package com.example.medisynchealthcareapplication;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Calendar;

public class diabeticActivity extends AppCompatActivity {

    EditText dateInput, emptyInput, fullInput;
    Button submitBtn, bBack;
    TextView commentText;
    LineChart diabetesChart;

    ArrayList<Entry> entries = new ArrayList<>();
    int counter = 0; // used as X-axis

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diabetic);

        dateInput = findViewById(R.id.dateInput);
        emptyInput = findViewById(R.id.emptyStomachInput);
        fullInput = findViewById(R.id.fullStomachInput);
        submitBtn = findViewById(R.id.submitBtn);
        commentText = findViewById(R.id.commentText);
        diabetesChart = findViewById(R.id.diabetesChart);
        bBack = findViewById(R.id.btnBack);

        // Handle back button
        bBack.setOnClickListener(view -> {
            finish(); // or go to DashboardActivity
        });

        // Handle date selection
        dateInput.setOnClickListener(view -> showDatePicker());

        // Handle submission
        submitBtn.setOnClickListener(view -> handleSubmission());

        // Apply padding for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(diabeticActivity.this, (view, year1, month1, dayOfMonth) -> {
            String selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
            dateInput.setText(selectedDate);
        }, year, month, day).show();
    }

    private void handleSubmission() {
        String emptyText = emptyInput.getText().toString();
        String fullText = fullInput.getText().toString();

        if (emptyText.isEmpty() || fullText.isEmpty()) {
            commentText.setText("Please enter both sugar levels.");
            commentText.setTextColor(getResources().getColor(R.color.colorRed));
            return;
        }

        float emptyValue = Float.parseFloat(emptyText);
        float fullValue = Float.parseFloat(fullText);

        entries.add(new Entry(counter++, (emptyValue + fullValue) / 2)); // Average for graph

        LineDataSet dataSet = new LineDataSet(entries, "Diabetes Level");
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(5f);
        dataSet.setColor(getResources().getColor(R.color.firebrick)); // Line color
        dataSet.setCircleColor(getResources().getColor(R.color.darkRed)); // Dots
        dataSet.setValueTextColor(getResources().getColor(R.color.purpleDark)); // Value text
        dataSet.setValueTextSize(12f);

        LineData lineData = new LineData(dataSet);
        diabetesChart.setData(lineData);
        diabetesChart.getDescription().setText("Diabetes Monitoring");
        diabetesChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        diabetesChart.invalidate(); // refresh

        // Set status
        String status = getStatus(emptyValue, fullValue);
        commentText.setText("Status: " + status);

        if (status.equals("Good")) {
            commentText.setTextColor(getResources().getColor(R.color.teal_200));
        } else if (status.equals("Low Sugar")) {
            commentText.setTextColor(getResources().getColor(R.color.pastelRed));
        } else {
            commentText.setTextColor(getResources().getColor(R.color.colorRed));
        }
    }

    private String getStatus(float empty, float full) {
        if (empty < 70 || full < 90) {
            return "Low Sugar";
        } else if ((empty >= 70 && empty <= 100) && (full >= 90 && full <= 140)) {
            return "Good";
        } else {
            return "High Sugar";
        }
    }
}
