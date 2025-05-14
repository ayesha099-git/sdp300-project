package com.example.medisynchealthcareapplication;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

public class AppointmentActivity extends AppCompatActivity {

    EditText editTextPatientName, editTextEmail, editTextDate, editTextTime;
    Button buttonConfirmAppointment;
    String doctorName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        editTextPatientName = findViewById(R.id.editTextPatientName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextDate = findViewById(R.id.editTextDate);
        editTextTime = findViewById(R.id.editTextTime);
        buttonConfirmAppointment = findViewById(R.id.buttonConfirmAppointment);

        String rawDoctorName = getIntent().getStringExtra("line1");
        doctorName = rawDoctorName.replace("Doctor Name : Dr. ", "").trim();


        editTextDate.setOnClickListener(v -> showDatePicker());
        editTextTime.setOnClickListener(v -> showTimePicker());

        buttonConfirmAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextPatientName.getText().toString();
                String email = editTextEmail.getText().toString();
                String date = editTextDate.getText().toString();
                String time = editTextTime.getText().toString();


                if (name.isEmpty() || email.isEmpty() || date.isEmpty() || time.isEmpty()) {
                    Toast.makeText(AppointmentActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                String url = "https://script.google.com/macros/s/AKfycbxQvGshOGoO2AA_ekZdOM8AiVRbjo_fRRwYRNUgNXaAoC3JsX2hhP8FgZZGkFznOuC0/exec" +
                        "?name=" + Uri.encode(name) +
                        "&email=" + Uri.encode(email) +
                        "&doctor=" + Uri.encode(doctorName) +
                        "&date=" + Uri.encode(date) +
                        "&time=" + Uri.encode(time);

                RequestQueue queue = Volley.newRequestQueue(AppointmentActivity.this);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(AppointmentActivity.this, "Appointment Requested", Toast.LENGTH_LONG).show();
                                Log.d("RESPONSE", response);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AppointmentActivity.this, "Failed to book appointment", Toast.LENGTH_LONG).show();
                        Log.e("ERROR", error.toString());
                    }
                });

                queue.add(stringRequest);
            }
        });

    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) ->
                editTextDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year),
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog dialog = new TimePickerDialog(this, (view, hourOfDay, minute) ->
                editTextTime.setText(String.format("%02d:%02d", hourOfDay, minute)),
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        dialog.show();
    }

    private void sendAppointmentToSheet(String patientName, String email, String date, String time, String doctorName) {
        AsyncTask.execute(() -> {
            try {
                URL url = new URL("https://script.google.com/macros/s/AKfycbxQvGshOGoO2AA_ekZdOM8AiVRbjo_fRRwYRNUgNXaAoC3JsX2hhP8FgZZGkFznOuC0/exec");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                JSONObject postData = new JSONObject();
                postData.put("patientName", patientName);
                postData.put("email", email);
                postData.put("date", date);
                postData.put("time", time);
                postData.put("doctorName", doctorName);
                postData.put("status", "Pending");

                OutputStream os = connection.getOutputStream();
                os.write(postData.toString().getBytes());
                os.flush();
                os.close();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    runOnUiThread(() -> Toast.makeText(this, "Appointment Request Sent", Toast.LENGTH_SHORT).show());
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Error sending request", Toast.LENGTH_SHORT).show());
            }
        });
    }
}