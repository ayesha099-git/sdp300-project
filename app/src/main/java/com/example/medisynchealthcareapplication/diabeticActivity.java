package com.example.medisynchealthcareapplication;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.app.DatePickerDialog;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import java.util.Calendar;
import java.util.ArrayList;

public class diabeticActivity extends AppCompatActivity {

    EditText dateInput, emptyInput, fullInput;
    Button submitBtn, bBack;
    TextView commentText;
    LineChart diabetesChart;

    Database database;
    String loggedInUsername;

    ArrayList<Entry> entries = new ArrayList<>();
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diabetic);

        database = new Database(this);

        // Get username safely from intent or fallback to empty string
        loggedInUsername = getIntent().getStringExtra("username");
        if (loggedInUsername == null) loggedInUsername = "";

        dateInput = findViewById(R.id.dateInput);
        emptyInput = findViewById(R.id.emptyStomachInput);
        fullInput = findViewById(R.id.fullStomachInput);
        submitBtn = findViewById(R.id.submitBtn);
        commentText = findViewById(R.id.commentText);
        diabetesChart = findViewById(R.id.diabetesChart);
        bBack = findViewById(R.id.btnBack);

        bBack.setOnClickListener(view -> finish());

        dateInput.setOnClickListener(view -> showDatePicker());

        submitBtn.setOnClickListener(view -> handleSubmission());

        // Load previous data safely and display on chart
        loadPreviousData();

        // Apply padding for system bars if your root layout has id "main"
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loadPreviousData() {
        entries.clear();
        counter = 0;

        Cursor cursor = null;
        try {
            cursor = database.getDiabeticData(loggedInUsername);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    // Defensive getString/getFloat with column index check
                    int dateIndex = cursor.getColumnIndex("date");
                    int emptyIndex = cursor.getColumnIndex("empty_sugar");
                    int fullIndex = cursor.getColumnIndex("full_sugar");
                    if (dateIndex == -1 || emptyIndex == -1 || fullIndex == -1) continue;

                    float emptySugar = cursor.getFloat(emptyIndex);
                    float fullSugar = cursor.getFloat(fullIndex);
                    float avg = (emptySugar + fullSugar) / 2;

                    entries.add(new Entry(counter++, avg));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }

        refreshChart();

        // Show status of last record if entries exist
        if (!entries.isEmpty()) {
            Cursor cursorLast = null;
            try {
                cursorLast = database.getDiabeticData(loggedInUsername);
                if (cursorLast != null && cursorLast.moveToLast()) {
                    int emptyIndex = cursorLast.getColumnIndex("empty_sugar");
                    int fullIndex = cursorLast.getColumnIndex("full_sugar");

                    if (emptyIndex != -1 && fullIndex != -1) {
                        @SuppressLint("Range") float lastEmpty = cursorLast.getFloat(emptyIndex);
                        @SuppressLint("Range") float lastFull = cursorLast.getFloat(fullIndex);

                        String status = getStatus(lastEmpty, lastFull);
                        commentText.setText("Status: " + status);
                        setCommentColor(status);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursorLast != null) cursorLast.close();
            }
        }
    }

    private void refreshChart() {
        if (entries.isEmpty()) {
            diabetesChart.clear();
            diabetesChart.invalidate();
            return;
        }

        LineDataSet dataSet = new LineDataSet(entries, "Diabetes Level");
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(5f);
        // Use ContextCompat to get color (modern safe way)
        dataSet.setColor(getColorSafe(R.color.firebrick));
        dataSet.setCircleColor(getColorSafe(R.color.darkRed));
        dataSet.setValueTextColor(getColorSafe(R.color.purpleDark));
        dataSet.setValueTextSize(12f);

        LineData lineData = new LineData(dataSet);
        diabetesChart.setData(lineData);
        diabetesChart.getDescription().setText("Diabetes Monitoring");
        diabetesChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        diabetesChart.invalidate();
    }

    private void handleSubmission() {
        String date = dateInput.getText().toString();
        String emptyText = emptyInput.getText().toString();
        String fullText = fullInput.getText().toString();

        if (date.isEmpty()) {
            commentText.setText("Please select a date.");
            commentText.setTextColor(getColorSafe(R.color.colorRed));
            return;
        }

        if (emptyText.isEmpty() || fullText.isEmpty()) {
            commentText.setText("Please enter both sugar levels.");
            commentText.setTextColor(getColorSafe(R.color.colorRed));
            return;
        }

        float emptyValue;
        float fullValue;

        try {
            emptyValue = Float.parseFloat(emptyText);
            fullValue = Float.parseFloat(fullText);
        } catch (NumberFormatException e) {
            commentText.setText("Please enter valid numbers for sugar levels.");
            commentText.setTextColor(getColorSafe(R.color.colorRed));
            return;
        }

        // Insert into DB
        boolean inserted = database.insertDiabeticData(loggedInUsername, date, emptyValue, fullValue);

        if (!inserted) {
            commentText.setText("Failed to save data. Try again.");
            commentText.setTextColor(getColorSafe(R.color.colorRed));
            return;
        }

        // Reload all data including new entry
        loadPreviousData();

        // Show comment
        String status = getStatus(emptyValue, fullValue);
        commentText.setText("Status: " + status);
        setCommentColor(status);
    }

    private void setCommentColor(String status) {
        switch (status) {
            case "Good":
                commentText.setTextColor(getColorSafe(R.color.teal_200));
                break;
            case "Low Sugar":
                commentText.setTextColor(getColorSafe(R.color.pastelRed));
                break;
            case "High Sugar":
                commentText.setTextColor(getColorSafe(R.color.colorRed));
                break;
            case "Medium":
            case "Pre-diabetic":
                commentText.setTextColor(getColorSafe(R.color.orange));
                break;
            default:
                commentText.setTextColor(getColorSafe(R.color.black));
        }
    }

    private String getStatus(float empty, float full) {
        float avg = (empty + full) / 2;

        if (avg < 70) return "Low Sugar";
        else if (avg <= 100) return "Good";
        else if (avg <= 125) return "Pre-diabetic";
        else if (avg <= 140) return "Medium";
        else return "High Sugar";
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

    // Helper method for modern color retrieval
    private int getColorSafe(int colorResId) {
        return getColor(colorResId); // requires API 23+, else use ContextCompat.getColor(this, colorResId)
    }
}
