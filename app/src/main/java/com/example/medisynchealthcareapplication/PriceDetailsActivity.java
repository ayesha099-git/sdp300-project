package com.example.medisynchealthcareapplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PriceDetailsActivity extends AppCompatActivity {

    private TextView textSelectedTest;
    private RecyclerView recyclerViewHospitalPrices;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_details);

        textSelectedTest = findViewById(R.id.textSelectedTest);
        recyclerViewHospitalPrices = findViewById(R.id.recyclerViewHospitalPrices);
        recyclerViewHospitalPrices.setLayoutManager(new LinearLayoutManager(this));

        // Get test name passed from LabtestActivity
        String testName = getIntent().getStringExtra("TEST_NAME");
        if (testName == null) testName = "Unknown Test";

        textSelectedTest.setText("Price Details for: " + testName);

        // Prepare data: hospital prices for this test
        List<HospitalPrice> hospitalPriceList = getHospitalPricesForTest(testName);

        // Set adapter with click listener for appointment dialog
        String finalTestName = testName;
        HospitalPriceAdapter adapter = new HospitalPriceAdapter(this, hospitalPriceList, (hospitalName, price) -> {
            showAppointmentDialog(hospitalName, finalTestName, price);
        });
        recyclerViewHospitalPrices.setAdapter(adapter);

    }

    public void showAppointmentDialog(String hospitalName, String testName, String price) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_appointment); // Make sure this layout XML exists

        EditText editEmail = dialog.findViewById(R.id.editEmail);
        Button buttonPickDate = dialog.findViewById(R.id.buttonPickDate);
        TextView textSelectedDate = dialog.findViewById(R.id.textSelectedDate);
        Button buttonPickTime = dialog.findViewById(R.id.buttonPickTime);
        TextView textSelectedTime = dialog.findViewById(R.id.textSelectedTime);
        Button buttonOk = dialog.findViewById(R.id.buttonOk);

        final String[] selectedDate = {null};
        final String[] selectedTime = {null};

        buttonPickDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year, month, dayOfMonth) -> {
                        selectedDate[0] = year + "-" + (month + 1) + "-" + dayOfMonth;
                        textSelectedDate.setText(selectedDate[0]);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        buttonPickTime.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    (view, hourOfDay, minute) -> {
                        selectedTime[0] = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                        textSelectedTime.setText(selectedTime[0]);
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true);
            timePickerDialog.show();
        });

        buttonOk.setOnClickListener(v -> {
            String email = editEmail.getText().toString().trim();
            if (email.isEmpty() || selectedDate[0] == null || selectedTime[0] == null) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            sendAppointmentToGoogleSheet(email, hospitalName, testName, price, selectedDate[0], selectedTime[0]);

            dialog.dismiss();
            Toast.makeText(this, "Appointment request sent. Status: Pending", Toast.LENGTH_SHORT).show();
        });

        dialog.show();
    }

    // Dummy method - you should implement your Google Apps Script POST here
    private void sendAppointmentToGoogleSheet(String email, String hospitalName, String testName,
                                              String price, String date, String time) {

        new Thread(() -> {
            try {
                // Replace with your actual Apps Script URL
                URL url = new URL("https://script.google.com/macros/s/AKfycbwLBJfV6S5Nkyb47bFsNTeCBDtJGT08avh3KsjzjnnhhCYVKJopMoKNrfZHuhtrAhV9aw/exec");

                // 👇 This line might have caused the error if 'url' wasn't a URL object
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                String postData = "email=" + URLEncoder.encode(email, "UTF-8") +
                        "&hospital=" + URLEncoder.encode(hospitalName, "UTF-8") +
                        "&test=" + URLEncoder.encode(testName, "UTF-8") +
                        "&price=" + URLEncoder.encode(price, "UTF-8") +
                        "&date=" + URLEncoder.encode(date, "UTF-8") +
                        "&time=" + URLEncoder.encode(time, "UTF-8");

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(postData);
                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();
                Log.d("GoogleSheetResponse", "Response Code: " + responseCode);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }


    private List<HospitalPrice> getHospitalPricesForTest(String testName) {
        Map<String, Map<String, Double>> priceData = new HashMap<>();

        Map<String, Double> cbcPrices = new HashMap<>();
        cbcPrices.put("City Hospital", 550.0);
        cbcPrices.put("Care Plus Clinic", 600.0);
        cbcPrices.put("Medicare Center", 580.0);

        Map<String, Double> denguePrices = new HashMap<>();
        denguePrices.put("City Hospital", 1200.0);
        denguePrices.put("Care Plus Clinic", 1150.0);
        denguePrices.put("Medicare Center", 1250.0);

        Map<String, Double> ultrasoundPrices = new HashMap<>();
        ultrasoundPrices.put("City Hospital", 1500.0);
        ultrasoundPrices.put("Care Plus Clinic", 1400.0);
        ultrasoundPrices.put("Medicare Center", 1600.0);

        priceData.put("CBC Test", cbcPrices);
        priceData.put("Dengue Test", denguePrices);
        priceData.put("Ultrasonography", ultrasoundPrices);

        Map<String, Double> defaultPrices = new HashMap<>();
        defaultPrices.put("City Hospital", 1000.0);
        defaultPrices.put("Care Plus Clinic", 1000.0);
        defaultPrices.put("Medicare Center", 1000.0);

        Map<String, Double> hospitalsPrices = priceData.getOrDefault(testName, defaultPrices);

        List<HospitalPrice> list = new ArrayList<>();
        for (Map.Entry<String, Double> entry : hospitalsPrices.entrySet()) {
            list.add(new HospitalPrice(entry.getKey(), entry.getValue()));
        }

        return list;
    }
}
